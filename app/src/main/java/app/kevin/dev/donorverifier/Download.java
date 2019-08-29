package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

import app.kevin.dev.donorverifier.adapters.RegionDownloadAdapter;
import app.kevin.dev.donorverifier.adapters.RegionDownloadAdapterCheckClickListener;
import app.kevin.dev.donorverifier.adapters.RegionDownloadAdapterDownloadClickListener;
import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.DownloadState;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.api_response.TotalApiResponse;
import app.kevin.dev.donorverifier.models.api_response.UpdateCountResponse;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.Sort;

public class Download extends AppCompatActivity implements RegionDownloadAdapterCheckClickListener, RegionDownloadAdapterDownloadClickListener {

    Realm realm;
    ListView regionList;
    ArrayList<Region> regions = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        checkForTotalCount();

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        AppDrawerItemClickListener.prepareAppDrawer(this);

        regions.add(new Region("XX","Deferred Donors", ""));
        regions.add(new Region("00","Region not set", ""));
        regions.addAll(Region.getRegions());

        regionList = findViewById(R.id.regionList);

        loadRegions();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkForTotalCount() {
        String total_donors = Session.get(this,"total_donors",null);
        String now = UserFn.getCurrentDateString();
        String lastCheck = Session.get(this,"total_donors_last_check","2013-01-01");
        Period diff = Period.between(LocalDate.parse(lastCheck).withDayOfMonth(1),LocalDate.parse(now).withDayOfMonth(1));

        if(total_donors == null){
            getTotalCount();
        }else if(diff.getMonths() > 0){
            getTotalCount();
        }
    }

    private void getTotalCount() {
        Api.call(this, UserFn.url(UserFn.API_GET_TOTAL), new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                TotalApiResponse apiResponse = UserFn.gson.fromJson(response.toString(),TotalApiResponse.class);
                Session.set(Download.this,"total_donors",String.valueOf(apiResponse.getCount()));
                Session.set(Download.this,"total_donors_with_photos",String.valueOf(apiResponse.getWithPhotos()));
                Session.set(Download.this,"total_deferred",String.valueOf(apiResponse.getDeferred()));
                Session.set(Download.this,"total_donors_last_check",UserFn.getCurrentDateString());
            }
        });
    }

    private void loadRegions() {
        RegionDownloadAdapter adapter = new RegionDownloadAdapter(this,regions,this,this);
        regionList.setAdapter(adapter);
        regionList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(final Region region, View convertView, boolean isCheckButton) {
        final TextView lastUpdate = convertView.findViewById(R.id.lastUpdate);
        final Button refresh = convertView.findViewById(R.id.refresh);

        Toast.makeText(this, "Please wait, checking..", Toast.LENGTH_SHORT).show();

        String url = getDownloadUrl(region);

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UpdateCountResponse countResponse = UserFn.gson.fromJson(response.toString(),UpdateCountResponse.class);
                if(countResponse.getStatus().equals("ok")){
                    UpdateCountResponse.DataResponse data = countResponse.getData();

                    lastUpdate.setText(String.valueOf(data.getCount()) + " Donors");
                    Session.set(Download.this,"last_remaining_"+region.getRegcode(),String.valueOf(data.getCount()));
                    Session.set(Download.this,"last_update_id_"+region.getRegcode(),getLastSeqno(region));


                }
            }
        });
    }

    @Override
    public void onClick(final Region region, View convertView) {
        Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show();

        String url = getDownloadUrl(region);

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UpdateCountResponse countResponse = UserFn.gson.fromJson(response.toString(),UpdateCountResponse.class);
                if(countResponse.getStatus().equals("ok")){
                    UpdateCountResponse.DataResponse data = countResponse.getData();
                    gotoDownloadRegion(region,data);
                }
            }
        });
    }

    private String getDownloadUrl(Region region){

        if(region.getRegcode().equals("XX")){
            final String last = getLastSeqno(region);
            String url = UserFn.url(UserFn.API_GET_DEFERRED_COUNT);
            url = url.replace("{last}",last);
            return url;
        }else{
            final String last = getLastSeqno(region);
            String url = UserFn.url(UserFn.API_GET_UPDATE_COUNT);
            url = url.replace("{regcode}", UserFn.urlEncode(region.getRegcode()));
            url = url.replace("{last}",last);
            return url;
        }
    }

    private String getLastSeqno(Region region){
        RealmQuery query = realm.where(Donor.class);
        if(region.getRegcode().equals("XX")){
            query.isNull("region");
            query.equalTo("donation_stat","N");
        }else{
            query.equalTo("region",region.getDbname(),Case.INSENSITIVE);
            query.in("donation_stat",new String[]{"Y","",null});
        }
        Donor donorLast = (Donor) query.sort("seqno",Sort.DESCENDING).findFirst();

        String lastSn = "0";
        if(donorLast != null){
            lastSn = donorLast.getSeqno();
        }
        return lastSn;
    }

    private void gotoDownloadRegion(Region region, UpdateCountResponse.DataResponse data) {
        if(data.getCount() == 0){
            Toast.makeText(this, "Nothing to download..", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this,DownloadRegion.class);
        intent.putExtra("region",UserFn.gson.toJson(region));
        intent.putExtra("donors",data.getCount());
        intent.putExtra("photos",data.getWithPhoto());
        startActivity(intent);
    }
}

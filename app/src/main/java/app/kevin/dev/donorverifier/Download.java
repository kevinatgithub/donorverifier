package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.adapters.RegionDownloadAdapter;
import app.kevin.dev.donorverifier.adapters.RegionDownloadAdapterCheckClickListener;
import app.kevin.dev.donorverifier.adapters.RegionDownloadAdapterDownloadClickListener;
import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.DownloadState;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.api_response.UpdateCountResponse;
import io.realm.Realm;

public class Download extends AppCompatActivity implements RegionDownloadAdapterCheckClickListener, RegionDownloadAdapterDownloadClickListener {

    Realm realm;
    ListView regionList;
    ArrayList<Region> regions = Region.getRegions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        Realm.init(this);
        realm = Realm.getDefaultInstance();

        AppDrawerItemClickListener.prepareAppDrawer(this);

        Region untagged = new Region("00","Region not set");
        regions.add(untagged);

        regionList = findViewById(R.id.regionList);

        loadRegions();
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
//        final TextView donorCount = convertView.findViewById(R.id.donorCount);
//        final TextView photoCount = convertView.findViewById(R.id.photoCount);

        Toast.makeText(this, "Please wait, checking..", Toast.LENGTH_SHORT).show();

        String last = Session.get(this,"last_update_id_"+region.getRegcode(),"0");
        String url = UserFn.url(UserFn.API_GET_UPDATE_COUNT);
        url = url.replace("{regcode}", UserFn.urlEncode(region.getRegcode()));
        url = url.replace("{last}",UserFn.urlEncode(last));

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UpdateCountResponse countResponse = UserFn.gson.fromJson(response.toString(),UpdateCountResponse.class);
                if(countResponse.getStatus().equals("ok")){
                    UpdateCountResponse.DataResponse data = countResponse.getData();
                    lastUpdate.setText(String.valueOf(data.getCount()) + " Donors");
                    Session.set(Download.this,"last_remaining_"+region.getRegcode(),String.valueOf(data.getCount()));
                }
            }
        });
    }

    @Override
    public void onClick(final Region region, View convertView) {
        Toast.makeText(this, "Please wait..", Toast.LENGTH_SHORT).show();
        DownloadState state = UserFn.getDownloadState(this);
        String last = "0";
        if(state.getRegions().size() > 0){
            for(DownloadState.RegionState r : state.getRegions()){
                if(r.getRegion().equals(region.getRegcode())){
                    last = r.getLast();
                }
            }
        }
        String url = UserFn.url(UserFn.API_GET_UPDATE_COUNT);
        url = url.replace("{regcode}", UserFn.urlEncode(region.getRegcode()));
        url = url.replace("{last}",UserFn.urlEncode(last));

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

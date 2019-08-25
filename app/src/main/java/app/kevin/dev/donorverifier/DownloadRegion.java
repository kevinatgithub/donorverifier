package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Barangay;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.DownloadState;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.api_response.BarangaysResponse;
import app.kevin.dev.donorverifier.models.api_response.CallbackWithStringResponse;
import app.kevin.dev.donorverifier.models.api_response.DonorPhotoResponse;
import app.kevin.dev.donorverifier.models.api_response.UpdateResponse;
import io.realm.Realm;
import io.realm.RealmResults;

public class DownloadRegion extends AppCompatActivity implements View.OnClickListener {

    Realm realm;

    TextView regionName;
    Button btnStartDownload;
    TextView downloadProgressText;
    ProgressBar downloadProgress;
    Region region;
    int donors;
    int photos;
    int barangays;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_region_download);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        realm = UserFn.getRealmInstance(this);

        regionName = findViewById(R.id.regionName);
        btnStartDownload = findViewById(R.id.btnStartDownload);
        downloadProgressText = findViewById(R.id.downloadProgressText);
        downloadProgress = findViewById(R.id.downloadProgress);

        Intent intent = getIntent();
        String strRegion = intent.getStringExtra("region");
        region = UserFn.gson.fromJson(strRegion,Region.class);
        donors = intent.getIntExtra("donors",0);
        photos = intent.getIntExtra("photos",0);
        barangays = intent.getIntExtra("barangays",0);

        regionName.setText(region.getRegname() + "\n" + String.valueOf(donors) + " Donors\n"+ String.valueOf(photos) + " Photos\n"  + String.valueOf(barangays) + " Barangays");
        downloadProgress.setMax(donors);
        downloadProgress.setProgress(0);

        btnStartDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginDownload();
            }
        });
    }

    private void beginDownload() {
        btnStartDownload.setVisibility(View.GONE);
        downloadProgress.setVisibility(View.VISIBLE);
        downloadProgressText.setVisibility(View.VISIBLE);
        downloadProgress.setIndeterminate(true);

        String url = UserFn.url(UserFn.API_BARANGAYS);
        url = url.replace("{regcode}",region.getRegcode());
        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                BarangaysResponse barangaysResponse = UserFn.gson.fromJson(response.toString(),BarangaysResponse.class);
                beginSavingBarangays(barangaysResponse.getData());
            }
        });

    }

    private void beginSavingBarangays(ArrayList<Barangay> data) {
        downloadProgress.setIndeterminate(false);
        realm.beginTransaction();
        for(Barangay barangay: data){
            realm.copyToRealmOrUpdate(barangay);
        }
        realm.commitTransaction();
        commenceDonorDownload();
    }

    private void commenceDonorDownload() {
        DownloadState state = UserFn.getDownloadState(this);
        String last = "0";
        if(state.getRegions().size() > 0){
            for(DownloadState.RegionState r : state.getRegions()){
                if(r.getRegion().equals(region.getRegcode())){
                    last = r.getLast();
                }
            }
        }
        String url = UserFn.url(UserFn.API_GET_UPDATE);
        url = url.replace("{regcode}", UserFn.urlEncode(region.getRegcode()));
        url = url.replace("{last}",UserFn.urlEncode(last));

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UpdateResponse updateResponse = UserFn.gson.fromJson(response.toString(),UpdateResponse.class);
                if(updateResponse.getStatus().equals("ok")){
                    beginSaving(updateResponse.getData());
                }
            }
        });
    }

    private String lastID = "0";
    private void beginSaving(ArrayList<Donor> data) {

        
        realm.beginTransaction();
        for(final Donor donor: data){

            lastID = donor.getSeqno();
            realm.copyToRealmOrUpdate(donor);

            int i = downloadProgress.getProgress();
            i++;
            downloadProgress.setProgress(i);

//            realm.executeTransactionAsync(new Realm.Transaction() {
//                @Override
//                public void execute(Realm realm) {
//                    realm.copyToRealmOrUpdate(donor);
//                }
//            }, new Realm.Transaction.OnSuccess() {
//                @Override
//                public void onSuccess() {
//                    int i = downloadProgress.getProgress();
//                    i++;
//                    downloadProgress.setProgress(i);
////                    downloadProgressText.setText("Downloading " +String.format("%.2f",i/downloadProgress.getMax()*100) + "%");
//                    if(i>=downloadProgress.getMax()){
//                        commenceSavingComplete();
//                    }
//                }
//            });
        }


        realm.commitTransaction();
        commencePhotoDownload();
    }

    private void commencePhotoDownload() {
        Toast.makeText(this, "Downloading photos", Toast.LENGTH_SHORT).show();
        RealmResults<Donor> donors = realm.where(Donor.class).equalTo("donor_photo","photo").findAll();
        for(final Donor donor:donors){
            String url = UserFn.url(UserFn.API_DONOR_PHOTO);
            url = url.replace("{seqno}",UserFn.urlEncode(donor.getSeqno()));
            Api.getString(this, url, new CallbackWithStringResponse() {
                @Override
                public void execute(@Nullable String response) {
                    realm.beginTransaction();
                    donor.setDonor_photo(response);
//                    realm.copyToRealmOrUpdate(donor);
                    realm.commitTransaction();
                }
            });
        }

        commenceSavingComplete();
    }

    private void commenceSavingComplete() {
        Toast.makeText(this, "Download complete", Toast.LENGTH_SHORT).show();
        DownloadState state = UserFn.getDownloadState(this);
        DownloadState.RegionState rs = new DownloadState.RegionState();
        for(DownloadState.RegionState rs2: state.getRegions()){
            if(rs2.getRegion().equals(region.getRegcode())){
                rs2.setLast(lastID);
                rs = rs2;
            }
        }

        rs.setRegion(region.getRegcode());
        rs.setLast(lastID);

        Session.set(this,"downloadState",UserFn.gson.toJson(state));
    }


    @Override
    public void onClick(View view) {
        finish();
    }
}

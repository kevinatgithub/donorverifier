package app.kevin.dev.donorverifier;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.RegionDataDownloader;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.ApiErrorCallback;
import app.kevin.dev.donorverifier.models.Callback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.DownloadState;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.api_response.CallbackWithStringResponse;
import app.kevin.dev.donorverifier.models.api_response.UpdateResponse;
import io.realm.Realm;
import io.realm.RealmResults;

public class DownloadRegion extends AppCompatActivity implements View.OnClickListener {

    private static final double PER_BATCH_COUNT = 100.0;
    Realm realm;

    TextView regionName;
    Button btnStartDownload;
    TextView downloadProgressText;
    ProgressBar downloadProgress;
    Region region;
    int donors;
    int photos;

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

        regionName.setText(region.getRegname() + "\n" + String.valueOf(donors) + " Donors\n"+ String.valueOf(photos) + " Photos");
        int max = (int) Math.ceil(donors/PER_BATCH_COUNT);
        downloadProgress.setMax(max);
        downloadProgress.setIndeterminate(false);
        downloadProgress.setProgress(0);

        btnStartDownload.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                btnStartDownload.setEnabled(false);
                downloadProgress.setVisibility(View.VISIBLE);
                batchDownloadRound(0);
            }
        });
    }

    interface Callback{
        void execute(int start);
    }
    boolean photosDone = false;
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void batchDownloadRound(int round){
        int max = (int) Math.ceil(donors/PER_BATCH_COUNT);
        int progress = round;
//        int currentP = ((int) (Math.ceil(progress/max) * 100));
        downloadProgress.setProgress(progress,true);
        if(progress <= max){
            commenceDonorDownload(progress, new Callback() {
                @Override
                public void execute(int start) {
                    batchDownloadRound(++start);
                }
            });
            return;
        }else{
            if(!photosDone){
                photosDone = true;
                commencePhotoDownload(new app.kevin.dev.donorverifier.models.Callback() {
                    @Override
                    public void execute() {
                    }
                });
            }
            Toast.makeText(DownloadRegion.this, "Download complete", Toast.LENGTH_SHORT).show();
        }

    }

    private void commenceDonorDownload(final int start, final Callback callback) {

//        RegionDataDownloader downloader = new RegionDataDownloader(this, start, new RegionDataDownloader.Callback() {
//            @Override
//            public void execute(JSONObject responseData) {
//                if(responseData != null){
//                    UpdateResponse request = UserFn.gson.fromJson(responseData.toString(),UpdateResponse.class);
//                    beginSaving(request.getData());
//                }
//                callback.execute(start);
//            }
//        });
//
//        downloader.execute(region.getRegcode());

        String url = UserFn.url(UserFn.API_GET_UPDATE_CHUNK);
        url = url.replace("{regcode}", UserFn.urlEncode(region.getRegcode()));
        String s = String.valueOf(start * (int) PER_BATCH_COUNT);
        url = url.replace("{start}", UserFn.urlEncode(s));
        url = url.replace("{size}", UserFn.urlEncode(String.valueOf((int) PER_BATCH_COUNT)));

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UpdateResponse updateResponse = UserFn.gson.fromJson(response.toString(), UpdateResponse.class);
                beginSaving(updateResponse.getData());
                callback.execute(start);
            }
        });
    }

    private void beginSaving(ArrayList<Donor> data) {
        for(final Donor donor: data){
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.copyToRealmOrUpdate(donor);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {

                }
            });
        }
    }

    private void commencePhotoDownload(final app.kevin.dev.donorverifier.models.Callback callback) {
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
                    realm.commitTransaction();
                    callback.execute();
                }
            });
        }

    }


    @Override
    public void onClick(View view) {
        finish();
    }
}

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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.RejectedExecutionException;

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
import io.realm.Sort;

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
        downloadProgressText.setVisibility(View.GONE);
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
                Date date = Calendar.getInstance().getTime();
                String dateStr = new SimpleDateFormat("MMMM dd, yyyy").format(date);
                Session.set(DownloadRegion.this,"last_update_" + region.getRegcode(), dateStr);
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
//        downloadProgressText.setText("Downloading " + progress + "%");
        if(progress <= max){
            commenceDonorDownload(progress, new Callback() {
                @Override
                public void execute(int start) {
                    batchDownloadRound(++start);
                }
            });
            return;
        }else{
            Toast.makeText(DownloadRegion.this, "Download complete.", Toast.LENGTH_SHORT).show();
            Donor last = realm.where(Donor.class).sort("seqno", Sort.DESCENDING).findFirst();
            Session.set(this,"last_update_id_" + region.getRegcode(),last.getSeqno());
        }

    }

    private void commenceDonorDownload(final int start, final Callback callback) {
       String url = getDownloadUrl(start);

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UpdateResponse updateResponse = UserFn.gson.fromJson(response.toString(), UpdateResponse.class);
                beginSaving(updateResponse.getData());
                callback.execute(start);
            }
        });
    }

    private String getDownloadUrl(int start) {
        if(region.getRegcode().equals("XX")){
            String last = Session.get(this,"last_update_id_"+region.getRegcode(),"0");
            String url = UserFn.url(UserFn.API_GET_DEFERRED_CHUNK);
            String s = String.valueOf(start * (int) PER_BATCH_COUNT);
            url = url.replace("{last}",UserFn.urlEncode(last));
            url = url.replace("{start}", UserFn.urlEncode(s));
            url = url.replace("{size}", UserFn.urlEncode(String.valueOf((int) PER_BATCH_COUNT)));
            return url;
        }else{
            String last = Session.get(this,"last_update_id_"+region.getRegcode(),"0");
            String url = UserFn.url(UserFn.API_GET_UPDATE_CHUNK);
            url = url.replace("{regcode}", UserFn.urlEncode(region.getRegcode()));
            String s = String.valueOf(start * (int) PER_BATCH_COUNT);
            url = url.replace("{last}",UserFn.urlEncode(last));
            url = url.replace("{start}", UserFn.urlEncode(s));
            url = url.replace("{size}", UserFn.urlEncode(String.valueOf((int) PER_BATCH_COUNT)));
            return url;
        }
    }

    private void beginSaving(ArrayList<Donor> data) {
        for(final Donor donor: data){
            try{
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if(donor.getDonor_photo() != null){
                            Log.d("PHOTO",donor.getDonor_photo());
                        }
                        realm.copyToRealmOrUpdate(donor);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {

                    }
                });
            }catch (RejectedExecutionException e){
                Log.e("Error saving",e.getMessage());
            }
        }
    }


    @Override
    public void onClick(View view) {
        finish();
    }
}

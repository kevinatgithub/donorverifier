package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
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
import app.kevin.dev.donorverifier.models.City;
import app.kevin.dev.donorverifier.models.DownloadState;
import app.kevin.dev.donorverifier.models.Province;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.StatisticsState;
import app.kevin.dev.donorverifier.models.api_response.CitiesResponse;
import app.kevin.dev.donorverifier.models.api_response.ProvincesResponse;
import app.kevin.dev.donorverifier.models.api_response.UpdateCountResponse;
import io.realm.Realm;

public class Download extends AppCompatActivity implements RegionDownloadAdapterCheckClickListener, RegionDownloadAdapterDownloadClickListener {

    Realm realm;
    CardView cardViewInit;
    Button btnDownloadInit;
    ProgressBar downloadingInitPB;
    TextView downloadingInit;
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

        cardViewInit = findViewById(R.id.card_view_init);
        btnDownloadInit = findViewById(R.id.btnDownloadInit);
        downloadingInitPB = findViewById(R.id.downloadingInitPB);
        downloadingInit = findViewById(R.id.downloadingInit);
        regionList = findViewById(R.id.regionList);

        findViewById(R.id.btnDownloadInit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                downloadInitResources();
            }
        });

        checkInitDownloadCompletion();
    }

    private void downloadInitResources() {
        btnDownloadInit.setVisibility(View.GONE);
        downloadingInitPB.setVisibility(View.VISIBLE);
        downloadingInit.setVisibility(View.VISIBLE);

        fetchProvincesThenCities();
    }

    private void fetchProvincesThenCities() {
        String url = UserFn.url(UserFn.API_PROVINCES);
        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                ProvincesResponse provincesResponse = UserFn.gson.fromJson(response.toString(),ProvincesResponse.class);
                if(provincesResponse.getStatus().equals("ok")){
                    saveProvinces(provincesResponse.getData());
                    fetchCities();
                }
            }
        });
    }

    private void fetchCities() {
        String url = UserFn.url(UserFn.API_CITIES);
        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                CitiesResponse citiesResponse = UserFn.gson.fromJson(response.toString(),CitiesResponse.class);
                if(citiesResponse.getStatus().equals("ok")){
                    saveCities(citiesResponse.getData());
                    flagThatInitDownloadComplete();
                }
            }
        });
    }

    private void flagThatInitDownloadComplete() {
        String str = Session.get(this,"statistics",null);
        StatisticsState state = new StatisticsState();
        if(str != null){
            state = UserFn.gson.fromJson(str,StatisticsState.class);
        }
        state.setProvinces(provinces);
        state.setCities(cities);
        Session.set(this,"statistics",UserFn.gson.toJson(state));
        cardViewInit.setVisibility(View.GONE);
        loadRegions();

    }

    private int provinces = 0;
    private int cities = 0;

    private void saveCities(ArrayList<City> data) {
        cities = data.size();
        realm.beginTransaction();
        for(City city:data){
            realm.copyToRealm(city);
        }
        realm.commitTransaction();
    }

    private void saveProvinces(ArrayList<Province> data) {
        provinces = data.size();
        realm.beginTransaction();
        for(Province province: data){
            realm.copyToRealm(province);
        }
        realm.commitTransaction();
    }

    private void checkInitDownloadCompletion() {
        String str = Session.get(this,"statistics",null);
        if(str != null){
            StatisticsState state = UserFn.gson.fromJson(str,StatisticsState.class);
            if(state.getProvinces() != 0 || state.getCities() !=0){
                cardViewInit.setVisibility(View.INVISIBLE);
                loadRegions();
            }

        }
    }

    private void loadRegions() {
        RegionDownloadAdapter adapter = new RegionDownloadAdapter(this,regions,this,this);
        regionList.setAdapter(adapter);
        regionList.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Region region, View convertView, boolean isCheckButton) {
        final TextView donorCount = convertView.findViewById(R.id.donorCount);
        final TextView barangayCount = convertView.findViewById(R.id.barangayCount);
        final TextView photoCount = convertView.findViewById(R.id.photoCount);
        final TextView donorsHidden = convertView.findViewById(R.id.donorsHidden);
        final TextView barangaysHidden = convertView.findViewById(R.id.barangaysHidden);

        TextView lastUpdate = convertView.findViewById(R.id.lastUpdate);

        Toast.makeText(this, "Please wait, checking..", Toast.LENGTH_SHORT).show();

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
                    donorCount.setText(String.valueOf(data.getCount()) + " Donors");
                    photoCount.setText(String.valueOf(data.getWithPhoto() + " Photos"));
                    barangayCount.setText(String.valueOf(data.getBarangays()) + " Barangays");
                    donorsHidden.setText(String.valueOf(data.getCount()));
                    barangaysHidden.setText(String.valueOf(data.getBarangays()));
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
        if(data.getCount() == 0 && data.getBarangays() == 0){
            Toast.makeText(this, "Nothing to download..", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this,DownloadRegion.class);
        intent.putExtra("region",UserFn.gson.toJson(region));
        intent.putExtra("donors",data.getCount());
        intent.putExtra("photos",data.getWithPhoto());
        intent.putExtra("barangays",data.getBarangays());
        startActivity(intent);
    }
}

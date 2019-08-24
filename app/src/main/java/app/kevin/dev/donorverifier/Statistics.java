package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Barangay;
import app.kevin.dev.donorverifier.models.Callback;
import app.kevin.dev.donorverifier.models.City;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.LocalDonor;
import app.kevin.dev.donorverifier.models.Province;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.StatisticsState;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class Statistics extends AppCompatActivity {

    TextView unsynced;
    TextView donors;
    TextView barangays;
    TextView cities;
    TextView provinces;
    TextView regions;
    ProgressBar unsyncedLoading;
    ProgressBar donorsLoading;
    ProgressBar barangaysLoading;
    ProgressBar citiesLoading;
    ProgressBar provincesLoading;
    ProgressBar regionsLoading;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        realm = UserFn.getRealmInstance(this);

        AppDrawerItemClickListener.prepareAppDrawer(this);

        unsynced = findViewById(R.id.unsynced);
        donors = findViewById(R.id.donors);
        barangays = findViewById(R.id.barangays);
        cities = findViewById(R.id.cities);
        provinces = findViewById(R.id.provinces);
        regions = findViewById(R.id.regions);
        unsyncedLoading = findViewById(R.id.unsyncedLoading);
        donorsLoading = findViewById(R.id.donorsLoading);
        barangaysLoading = findViewById(R.id.barangaysLoading);
        citiesLoading = findViewById(R.id.citiesLoading);
        provincesLoading = findViewById(R.id.provincesLoading);
        regionsLoading = findViewById(R.id.provincesLoading);

        loadDataFromState();

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCount();
            }
        });

        findViewById(R.id.cvRegions).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GeoList.class);
                i.putExtra("type","region");
                startActivity(i);
            }
        });

        findViewById(R.id.cvProvinces).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GeoList.class);
                i.putExtra("type","province");
                startActivity(i);
            }
        });

        findViewById(R.id.cvCities).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GeoList.class);
                i.putExtra("type","city");
                startActivity(i);
            }
        });

        findViewById(R.id.cvBarangays).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),GeoList.class);
                i.putExtra("type","barangay");
                startActivity(i);
            }
        });
    }

    private void refreshCount() {

        final StatisticsState state = new StatisticsState();

        final Cb allDoneCB = new Cb() {
            @Override
            public void execute(int cnt) {
                state.setRegions(cnt);
                regions.setText(String.valueOf(cnt));
                String str = UserFn.gson.toJson(state);
                Session.set(getApplicationContext(),"statistics",str);
                Toast.makeText(Statistics.this, "List has been refreshed", Toast.LENGTH_SHORT).show();
            }
        };

        final Cb regionsCB = new Cb(){
            @Override
            public void execute(int cnt) {
                allDoneCB.execute(cnt);
            }
        };

        final Cb provincesCB = new Cb() {
            @Override
            public void execute(int cnt) {
                state.setProvinces(cnt);
                provinces.setText(String.valueOf(cnt));
                getCountAsync(regionsLoading,Region.class,regionsCB);
            }
        };

        final Cb citiesCB = new Cb() {
            @Override
            public void execute(int cnt) {
                state.setCities(cnt);
                cities.setText(String.valueOf(cnt));
                getCountAsync(provincesLoading,Province.class,provincesCB);
            }
        };

        final Cb barangaysCB = new Cb(){
            @Override
            public void execute(int cnt) {
                state.setBarangays(cnt);
                barangays.setText(String.valueOf(cnt));
                getCountAsync(citiesLoading,City.class,citiesCB);
            }
        };

        final Cb donorsCB = new Cb(){
            @Override
            public void execute(int cnt) {
                state.setDonors(cnt);
                donors.setText(String.valueOf(cnt));
                getCountAsync(barangaysLoading,Barangay.class,barangaysCB);
            }
        };

        Cb unsyncedCB = new Cb() {
            @Override
            public void execute(int cnt) {
                state.setUnsynced(cnt);
                unsynced.setText(String.valueOf(cnt));
                getCountAsync(donorsLoading,Donor.class,donorsCB);
            }
        };

        getCountAsync(unsyncedLoading, LocalDonor.class, unsyncedCB);

    }

    private void getCountAsync(final ProgressBar pb, final Class cls, @Nullable final Cb cb){
        if(cls == Region.class){
            cb.execute(Region.getRegions().size());
            return;
        }
        pb.setVisibility(View.VISIBLE);
        RealmResults results = realm.where(cls).findAll();
        pb.setVisibility(View.INVISIBLE);
        cb.execute(results.size());

//        pb.setVisibility(View.VISIBLE);
//        final Count cnt = new Count();
//        realm.beginTransaction();
//        realm.executeTransactionAsync(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                cnt.result = realm.where(cls).findAll();
//            }
//        }, new Realm.Transaction.OnSuccess() {
//            @Override
//            public void onSuccess() {
//                pb.setVisibility(View.INVISIBLE);
//                if(cb != null){
//                    realm.commitTransaction();
//                    cb.execute(cnt.result.size());
//                }
//            }
//        });
    }

    private class Count{
        public RealmResults result;
    }

    private interface Cb{
        void execute(int cnt);
    }

    private void loadDataFromState() {
        String str = Session.get(this,"statistics",null);
        if(str != null){
            StatisticsState statisticsState = UserFn.gson.fromJson(str,StatisticsState.class);

            unsynced.setText(String.valueOf(statisticsState.getUnsynced()));
            donors.setText(String.valueOf(statisticsState.getDonors()));
            barangays.setText(String.valueOf(statisticsState.getBarangays()));
            cities.setText(String.valueOf(statisticsState.getCities()));
            provinces.setText(String.valueOf(statisticsState.getProvinces()));
        }
    }
}

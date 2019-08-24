package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.adapters.BarangayAdapter;
import app.kevin.dev.donorverifier.adapters.CityAdapter;
import app.kevin.dev.donorverifier.adapters.ProvinceAdapter;
import app.kevin.dev.donorverifier.adapters.RegionAdapter;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Barangay;
import app.kevin.dev.donorverifier.models.City;
import app.kevin.dev.donorverifier.models.Province;
import app.kevin.dev.donorverifier.models.Region;
import io.realm.Realm;
import io.realm.RealmResults;

public class GeoList extends AppCompatActivity implements View.OnClickListener {

    Realm realm;
    ListView lvReferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_list);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        realm = UserFn.getRealmInstance(this);

        final Intent intent = getIntent();
        String type = intent.getStringExtra("type");

        lvReferences = findViewById(R.id.lvReferences);

        switch (type){
            case "region":
                toolbar.setTitle("Regions");
                loadRegions();
                break;
            case "province":
                toolbar.setTitle("Provinces");
                loadProvinces();
                break;
            case "city":
                toolbar.setTitle("Cities / Municipalities");
                loadCities();
                break;
            case "barangay":
                toolbar.setTitle("Barangays");
                loadBarangays();
                break;
        }
    }

    private void loadBarangays() {
        RealmResults<Barangay> barangayResult = realm.where(Barangay.class).findAll();
        ArrayList<Barangay> barangays = new ArrayList<>();
        barangays.addAll(barangayResult);
        BarangayAdapter adapter = new BarangayAdapter(this,barangays);
        lvReferences.setAdapter(adapter);
    }

    private void loadCities() {
        RealmResults<City> cityResult = realm.where(City.class).findAll();
        ArrayList<City> cities = new ArrayList<>();
        cities.addAll(cityResult);
        CityAdapter adapter = new CityAdapter(this,cities);
        lvReferences.setAdapter(adapter);
    }

    private void loadProvinces() {
        RealmResults<Province> provincesResult = realm.where(Province.class).findAll();
        ArrayList<Province> provinces = new ArrayList<>();
        provinces.addAll(provincesResult);
        ProvinceAdapter adapter = new ProvinceAdapter(this,provinces);
        lvReferences.setAdapter(adapter);
    }

    private void loadRegions() {
        ArrayList<Region> regions = Region.getRegions();
        RegionAdapter adapter = new RegionAdapter(this,regions);
        lvReferences.setAdapter(adapter);
    }



    @Override
    public void onClick(View view) {
        finish();
    }
}

package app.kevin.dev.donorverifier;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.StatisticsState;
import io.realm.Realm;
import io.realm.RealmResults;

public class Statistics extends AppCompatActivity {

    private static final int TOTAL_DONORS = 10000;

    TextView donors;
    TextView photos;
    ProgressBar donorsLoading;
    ProgressBar photosLoading;
    Realm realm;
    PieChart pieDonors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        realm = UserFn.getRealmInstance(this);
        initCharts();

        AppDrawerItemClickListener.prepareAppDrawer(this);

        donors = findViewById(R.id.donors);
        photos = findViewById(R.id.photos);

        donorsLoading = findViewById(R.id.donorsLoading);
        photosLoading = findViewById(R.id.photosLoading);

        loadDataFromState();

        findViewById(R.id.btnRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refreshCount();
            }
        });
    }

    private void initCharts() {
        pieDonors = initPie(R.id.pieDonors);
    }

    private PieChart initPie(int id){
        PieChart chart = findViewById(id);
        chart.setBackgroundColor(Color.WHITE);
        chart.getDescription().setEnabled(false);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(Color.WHITE);

        chart.setTransparentCircleColor(Color.WHITE);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(61f);

        chart.setDrawCenterText(true);

        chart.setRotationEnabled(false);
        chart.setHighlightPerTapEnabled(true);

        chart.setMaxAngle(180f); // HALF CHART
        chart.setRotationAngle(180f);
        chart.setCenterTextOffset(0, -20);
        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        // entry label styling
        chart.setEntryLabelColor(Color.WHITE);
        chart.setEntryLabelTextSize(12f);
        return chart;
    }

    private void refreshCount() {

        final StatisticsState state = new StatisticsState();

        final Cb allDoneCB = new Cb() {
            @Override
            public void execute(int cnt) {
                String str = UserFn.gson.toJson(state);
                Session.set(getApplicationContext(),"statistics",str);
                Toast.makeText(Statistics.this, "List has been refreshed", Toast.LENGTH_SHORT).show();
                refreshChart();
            }
        };

        final Cb photosCB = new Cb(){
            @Override
            public void execute(int cnt) {
                state.setPhotos(cnt);
                photos.setText(String.valueOf(cnt));
                allDoneCB.execute(cnt);
            }
        };

        final Cb donorsCB = new Cb(){
            @Override
            public void execute(int cnt) {
                state.setDonors(cnt);
                donors.setText(String.valueOf(cnt));
                getCountAsync(photosLoading,Splash.class,photosCB);
            }
        };

        getCountAsync(donorsLoading, Donor.class, donorsCB);

    }

    private void refreshChart() {
        String str = Session.get(this,"statistics",null);
        StatisticsState state = new StatisticsState();
        if(str != null){
            state = UserFn.gson.fromJson(str,StatisticsState.class);
        }

        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(state.getDonors(),"Downloaded"));
        values.add(new PieEntry(TOTAL_DONORS - state.getDonors(),"Pending"));

        PieDataSet set = new PieDataSet(values,"");
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(android.R.color.holo_blue_light));
        colors.add(getResources().getColor(android.R.color.holo_orange_light));
        set.setColors(colors);
        PieData data = new PieData(set);
        data.setValueTextColor(getResources().getColor(android.R.color.white));
        data.setValueTextSize(14);
        pieDonors.setData(data);
        pieDonors.animate();
    }

    private void getCountAsync(final ProgressBar pb, final Class cls, @Nullable final Cb cb){

        // TODO: 25/08/2019 Replace Splash.class, it was only used temporary
        if(cls == Splash.class){
            RealmResults r = realm.where(Donor.class).isNotNull("donor_photo").findAll();
            cb.execute(r.size());
            return;
        }

        pb.setVisibility(View.VISIBLE);
        RealmResults results = realm.where(cls).findAll();
        pb.setVisibility(View.INVISIBLE);
        cb.execute(results.size());

    }

    private interface Cb{
        void execute(int cnt);
    }

    private void loadDataFromState() {
        String str = Session.get(this,"statistics",null);
        if(str != null){
            StatisticsState statisticsState = UserFn.gson.fromJson(str,StatisticsState.class);

            donors.setText(String.valueOf(statisticsState.getDonors()));
            photos.setText(String.valueOf(statisticsState.getPhotos()));
            refreshChart();
        }
    }
}

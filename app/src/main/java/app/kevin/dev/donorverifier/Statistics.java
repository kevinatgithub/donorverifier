package app.kevin.dev.donorverifier;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.Random;

import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.Region;
import app.kevin.dev.donorverifier.models.StatisticsState;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;

public class Statistics extends AppCompatActivity {

    private static int TOTAL_DONORS = 0, TOTAL_DEFERRED = 0;

    TextView donors;
    TextView photos;
    ProgressBar donorsLoading;
    ProgressBar photosLoading;
    Realm realm;
    PieChart pieDonors, pieDeffered;
    HorizontalBarChart barRegions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        TOTAL_DONORS = Integer.parseInt(Session.get(this,"total_donors","0"));
        TOTAL_DEFERRED = Integer.parseInt(Session.get(this,"total_deferred","0"));

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
        pieDonors = initPie(R.id.pieDonors); pieDeffered = initPie(R.id.pieDeferred);
        barRegions = initBar(R.id.barRegion);
    }

    private HorizontalBarChart initBar(int id) {
        HorizontalBarChart chart = findViewById(id);

        chart.setDrawBarShadow(true);

        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        chart.setPinchZoom(false);

        // draw shadows for each bar that show the maximum value
        // chart.setDrawBarShadow(true);

        chart.setDrawGridBackground(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setEnabled(false);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(false);
        xl.setGranularity(10f);

        YAxis yl = chart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yl.setInverted(true);

        YAxis yr = chart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//        yr.setInverted(true);

        chart.setFitBars(true);
        chart.animateY(2500);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);

        return chart;
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
                final int[] donors = {0};
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        donors[0] = realm.where(Donor.class).findAll().size();
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        refreshChart(donors[0]);
                        pieDonors.getData().notifyDataChanged();
                        pieDeffered.getData().notifyDataChanged();
                        barRegions.getData().notifyDataChanged();
                    }
                });
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

    private void refreshChart(int donors) {

//        int donors = realm.where(Donor.class).findAll().size();
//        String str = Session.get(this,"statistics",null);
//        StatisticsState state = new StatisticsState();
//        if(str != null){
//            state = UserFn.gson.fromJson(str,StatisticsState.class);
//        }

        // Donors vs All
        ArrayList<PieEntry> values = new ArrayList<>();
        values.add(new PieEntry(donors,"Downloaded"));
        values.add(new PieEntry(TOTAL_DONORS - donors,"Pending"));

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

        int deferred = realm.where(Donor.class).equalTo("donation_stat","N",Case.INSENSITIVE).findAll().size();
        // Donors vs Deferred
        ArrayList<PieEntry> values2 = new ArrayList<>();
        values2.add(new PieEntry(deferred,"Downloaded"));
        values2.add(new PieEntry(TOTAL_DEFERRED - deferred,"Pending"));

        PieDataSet set2 = new PieDataSet(values2,"");
        ArrayList<Integer> colors2 = new ArrayList<>();
        colors2.add(getResources().getColor(android.R.color.holo_green_light));
        colors2.add(getResources().getColor(android.R.color.holo_red_light));
        set2.setColors(colors2);
        PieData data2 = new PieData(set2);
        data2.setValueTextColor(getResources().getColor(android.R.color.white));
        data2.setValueTextSize(14);
        pieDeffered.setData(data2);
        pieDeffered.animate();

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        int i = 0;
        for(Region region: Region.getRegions()){
            int size = realm.where(Donor.class).equalTo("region",region.getDbname()).findAll().size();
            if(size > 0){
                Random rnd = new Random();
                int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                BarEntry barEntry = new BarEntry(i,size,region.getRegname());
                ArrayList<BarEntry> l = new ArrayList<>();
                l.add(barEntry);
                BarDataSet bds = new BarDataSet(l,region.getRegcode());
                bds.setColor(color);
                dataSets.add(bds);
                i++;
            }
        }

        BarData data3 = new BarData(dataSets);
        barRegions.setData(data3);
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
            final int[] donors = {0};
            realm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    donors[0] = realm.where(Donor.class).findAll().size();
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    refreshChart(donors[0]);
                }
            });
        }
    }
}

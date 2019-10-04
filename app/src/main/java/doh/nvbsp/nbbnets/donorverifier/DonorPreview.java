package doh.nvbsp.nbbnets.donorverifier;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.qap.ctimelineview.TimelineRow;
import org.qap.ctimelineview.TimelineViewAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.Donor;
import doh.nvbsp.nbbnets.donorverifier.models.Donation;
import io.realm.Realm;
import io.realm.RealmList;

public class DonorPreview extends AppCompatActivity implements View.OnClickListener {

    private Donor donor;
    private Realm realm;

    TextView donorID;
    ImageView photo;
    ImageView barcode;
    TextView donationStatus;
    TextView fullName;
    TextView bdate;
    TextView gender;
    TextView noStBlock;
    TextView barangay;
    TextView city;
    TextView province;
    TextView region;

    ListView donationTimeLine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_preview);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        realm = UserFn.getRealmInstance(this);

        final Intent intent = getIntent();
        String seqno = intent.getStringExtra("seqno");
        donor = realm.where(Donor.class).equalTo("seqno",seqno).findFirst();

        donorID = findViewById(R.id.donorID);
        photo = findViewById(R.id.photo);
        barcode = findViewById(R.id.barcode);
        donationStatus = findViewById(R.id.donationStatus);
        fullName = findViewById(R.id.txtValue);
        bdate = findViewById(R.id.txtKey);
        gender = findViewById(R.id.gender);
        noStBlock = findViewById(R.id.noStBlock);
        barangay = findViewById(R.id.barangay);
        city = findViewById(R.id.city);
        province = findViewById(R.id.province);
        region = findViewById(R.id.region);
        donationTimeLine = findViewById(R.id.donationTimeLine);


//        findViewById(R.id.btnUpdate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent1 = new Intent(getApplicationContext(),DonorForm.class);
//                intent1.putExtra("id",donor.getSeqno());
//                startActivity(intent1);
//                finish();
//            }
//        });

        if(donor != null)
            populateFields();

    }

    private void populateFields() {

        if(donor.getDonor_id() != null && !donor.getDonor_id().trim().isEmpty())
            donorID.setText(donor.getDonor_id());
        else
            donorID.setText("Donor ID not available");


        if(donor.getDonor_photo() != null){
            try{
                byte[] decodedString = Base64.decode(donor.getDonor_photo(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                photo.setImageBitmap(decodedByte);
            }catch (Exception e){

            }
        }else{
            photo.setVisibility(View.GONE);
        }

        if(donor.getBarcode() != null){
            byte[] decodedString = Base64.decode(donor.getBarcode(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            barcode.setImageBitmap(decodedByte);
            barcode.setBackgroundColor(getResources().getColor(android.R.color.white));
            barcode.setVisibility(View.VISIBLE);
        }else{
            barcode.setImageDrawable(getResources().getDrawable(android.R.drawable.title_bar));
            barcode.setVisibility(View.INVISIBLE);
        }

        donationStatus.setText("");

        if(donor.getDonation_stat() != null){
            if(donor.getDonation_stat().toUpperCase().equals("Y")){
                donationStatus.setText("May Donate");
                donationStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }else if(donor.getDonation_stat().toUpperCase().equals("N")){
                donationStatus.setText("Can't Donate");
                donationStatus.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }else{
                donationStatus.setText("May Donate");
                donationStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        }else{
            donationStatus.setText("May Donate");
            donationStatus.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        fullName.setText(UserFn.convertToTitleCaseIteratingChars(donor.getFname() + " " + donor.getMname() + " " + donor.getLname()));
        bdate.setText(donor.getBdate());
        if(donor.getGender().equals("M")){
            gender.setText("Male");
        }else if(donor.getGender().equals("F")){
            gender.setText("Female");
        }
        noStBlock.setText(donor.getHome_no_st_blk());

        barangay.setText(donor.getBarangay());
        city.setText(donor.getCity());
        province.setText(donor.getProvince());
        region.setText(donor.getRegion());

        try {
            populateDonations();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void populateDonations() throws ParseException {
        if(donor.getDonations().size() == 0){
            return;
        }
        ArrayList<TimelineRow> timelineRowsList = new ArrayList<>();

        int i = 0;
        RealmList<Donation> rdonations = donor.getDonations();
        ArrayList<Donation> donations = new ArrayList<>();
        donations.addAll(rdonations);
        Collections.reverse(donations);
        for(Donation donation: donations){
            TimelineRow r = new TimelineRow(i);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(donation.getDonation_dt());
            r.setDate(date);
            r.setTitle(donation.getFacility());
            r.setTitleColor(getResources().getColor(R.color.colorPrimary));
            r.setBellowLineColor(getResources().getColor(R.color.colorPrimary));
            Bitmap img = BitmapFactory.decodeResource(getResources(),R.drawable.ic_circle_black);
            r.setImage(img);
            i++;
            timelineRowsList.add(r);
        }

        ArrayAdapter<TimelineRow> myAdapter = new TimelineViewAdapter(this, 0, timelineRowsList,
                //if true, list will be sorted by date
                false);

        donationTimeLine.setAdapter(myAdapter);
    }

    @Override
    public void onClick(View view) {
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(donor.getDonation_stat().equals("N")){

        }else{
            getMenuInflater().inflate(R.menu.donor_preview_menu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_share:
                Intent intent = new Intent();
                intent.setAction("com.nbbnets.donorid.receive");
                intent.putExtra("fname",donor.getFname());
                intent.putExtra("mname",donor.getMname());
                intent.putExtra("lname",donor.getLname());
                intent.putExtra("region",donor.getRegion());
                intent.putExtra("bdate",donor.getBdate());
                intent.putExtra("photo",donor.getDonor_photo());
                try{
                    startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(this, "Donor ID App not found", Toast.LENGTH_LONG).show();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
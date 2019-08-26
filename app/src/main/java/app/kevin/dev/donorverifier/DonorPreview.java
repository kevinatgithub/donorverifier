package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Donor;
import io.realm.Realm;

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
        }else{
            barcode.setImageDrawable(getResources().getDrawable(android.R.drawable.title_bar));
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

    }

    @Override
    public void onClick(View view) {
        finish();
    }
}

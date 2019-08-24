package app.kevin.dev.donorverifier;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Barangay;
import app.kevin.dev.donorverifier.models.City;
import app.kevin.dev.donorverifier.models.Donor;
import app.kevin.dev.donorverifier.models.LocalDonor;
import app.kevin.dev.donorverifier.models.Province;
import app.kevin.dev.donorverifier.models.Region;
import io.realm.Realm;

public class DonorForm extends AppCompatActivity implements View.OnClickListener {

    private Donor donor;
    private Realm realm;

    TextInputLayout lFname;
    EditText fname;
    TextInputLayout lMname;
    EditText mname;
    TextInputLayout lLname;
    EditText lname;
    Spinner gender;
    TextInputLayout lbdate;
    EditText bdate;
    TextInputLayout lAddress;
    EditText address;
    TextInputLayout lNoStBlk;
    EditText noStBlk;
    ImageView photo;
    Button btnSave;
    Button btnCancel;
    ArrayAdapter<String> genderAdapter;
    Calendar myCalendar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_form);

        Toolbar toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(this);

        realm = UserFn.getRealmInstance(this);

        Intent intent = getIntent();
        if(!intent.hasExtra("id")){
            donor = new Donor();
        }else{
            donor = realm.where(Donor.class).equalTo("seqno",intent.getStringExtra("id")).findFirst();
        }

        lFname = findViewById(R.id.tilFname);
        fname = findViewById(R.id.txtFname);
        lMname = findViewById(R.id.tilMname);
        mname = findViewById(R.id.txtMname);
        lLname = findViewById(R.id.tilLname);
        lname = findViewById(R.id.txtLname);
        gender = findViewById(R.id.gender);
        lbdate = findViewById(R.id.tilBdate);
        bdate = findViewById(R.id.txtBdate);
        lNoStBlk = findViewById(R.id.tilNoStBlk);
        noStBlk = findViewById(R.id.txtNoStBlk);
        lAddress = findViewById(R.id.tilAddress);
        address = findViewById(R.id.txtAddress);
        photo = findViewById(R.id.photo);
        List<String> spinnerArray =  new ArrayList<String>();
        spinnerArray.add("Male");
        spinnerArray.add("Female");

        genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        gender.setAdapter(genderAdapter);

        myCalendar = Calendar.getInstance();

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateBdateValue();
            }

        };

        bdate.setKeyListener(null);
        bdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(DonorForm.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        address.setKeyListener(null);

        findViewById(R.id.btnSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });

        findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        if(intent.hasExtra("id")){
            populateFields();
        }

    }

    private void validateForm() {
        int errors = 0;

        lFname.setError(null);
        lLname.setError(null);
        lbdate.setError(null);

        if(fname.getText().toString().length() == 0){
            lFname.setError("First name is required");
            errors+=1;
        }

        if(lname.getText().toString().length() == 0){
            lLname.setError("Last name is required");
            errors+=1;
        }

        if(bdate.getText().toString().length() == 0){
            lbdate.setError("Birth date is required");
            errors+=1;
        }

        if(errors == 0){
            collectFormData();
        }
    }

    private void collectFormData() {
        realm.beginTransaction();
        donor.setFname(fname.getText().toString());
        donor.setMname(mname.getText().toString());
        donor.setLname(lname.getText().toString());
        donor.setBdate(bdate.getText().toString());
        if(gender.getSelectedItem().equals("Male"))
            donor.setGender("M");
        else
            donor.setGender("F");
        donor.setHome_no_st_blk(noStBlk.getText().toString());
        realm.commitTransaction();

        saveUnsyncedDonor();
    }

    private void saveUnsyncedDonor() {
//        LocalDonor ldonor = realm.createObject(LocalDonor.class,donor);
        LocalDonor ldonor = LocalDonor.convert(donor,realm);
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(ldonor);
        realm.commitTransaction();
        Toast.makeText(this, "Donor record save.", Toast.LENGTH_LONG).show();
        finish();
    }

    private void updateBdateValue() {
        String myFormat = "YYYY-mm-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        bdate.setText(sdf.format(myCalendar.getTime()));
    }

    private void populateFields() {
        fname.setText(donor.getFname());
        mname.setText(donor.getMname());
        lname.setText(donor.getLname());
        if(donor.getGender().equals("M")){
            gender.setSelection(genderAdapter.getPosition("Male"));
        }else{
            gender.setSelection(genderAdapter.getPosition("Female"));
        }
        bdate.setText(donor.getBdate());
        noStBlk.setText(donor.getHome_no_st_blk());

        String adrs = parseAddress(donor);

        address.setText(adrs);
    }

    private String parseAddress(Donor donor) {
        String adrs = "";

        if(donor.getHome_brgy() != null){
            Barangay bgy = realm.where(Barangay.class).equalTo("bgycode",donor.getHome_brgy()).findFirst();
            if(bgy != null)
                adrs += bgy.getBgyname();
        }

        if(donor.getHome_city() != null){
            City cty = realm.where(City.class).equalTo("citycode",donor.getHome_city()).findFirst();
            if(cty != null)
                adrs += ", " + cty.getCityname();
        }

        if(donor.getHome_prov() != null){
            Province prov = realm.where(Province.class).equalTo("provcode",donor.getHome_prov()).findFirst();
            if(prov != null)
                adrs += ", " + prov.getProvname();
        }

        if(donor.getHome_region() != null){
            ArrayList<Region> regions = Region.getRegions();
            for(Region r2: regions){
                if(r2.getRegcode().equals(donor.getHome_region())){
                    adrs += ", " + r2.getRegname();
                }
            }
        }

        return adrs;
    }

    @Override
    public void onClick(View view) {
        finish();
    }
}

package doh.nvbsp.nbbnets.donorverifier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;

import java.util.ArrayList;
import java.util.List;

import doh.nvbsp.nbbnets.donorverifier.adapters.DonorRecyclerViewAdapter;
import doh.nvbsp.nbbnets.donorverifier.adapters.DonorRecyclerViewClickListener;
import doh.nvbsp.nbbnets.donorverifier.libs.Session;
import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.Donor;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DonorVerifier extends AppCompatActivity {

//    ListView resultList;
    RecyclerView resultList;
    RecyclerView.LayoutManager layoutManager;
    Button btnSearch, btnClear;
    EditText txtFname, txtLname, txtBdate;
    Realm realm;
    ConstraintLayout clInfo;
    TextView info;
    CardView cardView;
    Switch includeMayDonate, withPhotoOnly;

    ArrayList<Donor> donors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_verifier);

        AppDrawerItemClickListener.prepareAppDrawer(this);
        realm = UserFn.getRealmInstance(this);

        cardView = findViewById(R.id.card_view);
        resultList = findViewById(R.id.donorList);
        resultList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        resultList.setLayoutManager(layoutManager);

        btnSearch = findViewById(R.id.btnSearch);
        btnClear = findViewById(R.id.btnClear);
        txtFname = findViewById(R.id.txtFname);
        txtLname = findViewById(R.id.txtLname);
        txtBdate = findViewById(R.id.txtDob);
        includeMayDonate = findViewById(R.id.switchIncludeAll);
        withPhotoOnly = findViewById(R.id.switchPhotoOnly);

        if (Build.VERSION.SDK_INT <= 22){
            includeMayDonate.setTextColor(getResources().getColor(android.R.color.black));
            withPhotoOnly.setTextColor(getResources().getColor(android.R.color.black));
        }

        UserFn.attachDatePicker(this, R.id.txtDob, new UserFn.DateSelectedListner() {
            @Override
            public void onSet(String date) {
                txtBdate.setText(date);
            }
        });
        clInfo = findViewById(R.id.cv_info);
        info = findViewById(R.id.info);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtFname.setText("");
                txtLname.setText("");
                txtBdate.setText("");
            }
        });

        switchScreenState();

        if(Session.get(this,"tip_donor_verifier_1",null) == null){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    List<TapTarget> targets = new ArrayList<>();
                    targets.add(UserFn.buildTapTargetToolbar(DonorVerifier.this,R.id.action_search,R.string.ttv_donor_verifier_1_title,R.string.ttv_donor_verifier_1_message));
                    targets.add(UserFn.buildTapTargetNavigationIcon(DonorVerifier.this,R.string.ttv_donor_verifier_2_title,R.string.ttv_donor_verifier_2_message));
                    new TapTargetSequence(DonorVerifier.this).targets(targets).start();
                    Session.set(DonorVerifier.this,"tip_donor_verifier_1","Y");
                }
            },200);
        }
    }

    private void gotoPreview(Donor donor) {
        Intent intent = new Intent(this,DonorPreview.class);
        intent.putExtra("seqno",donor.getSeqno());
        startActivity(intent);
    }

    private void performSearch() {
        String fname = txtFname.getText().toString();
        String lname = txtLname.getText().toString();
        String bdate = txtBdate.getText().toString();

        RealmQuery query = realm.where(Donor.class);

        if(fname.length()>0){
            query.contains("fname",fname, Case.INSENSITIVE);
        }

        if(lname.length()>0){
            query.contains("lname",lname, Case.INSENSITIVE);
        }

        if(bdate.length()>0){
            query.contains("bdate",bdate, Case.SENSITIVE);
        }

        if(!includeMayDonate.isChecked()){
            query.equalTo("donation_stat","N",Case.INSENSITIVE);
        }

        if(withPhotoOnly.isChecked()){
            query.isNotNull("donor_photo");
        }

        RealmResults<Donor> realmResults = query.findAll();

        donors = new ArrayList<>();
        donors.addAll(realmResults);

        switchScreenState();

        DonorRecyclerViewAdapter adapter = new DonorRecyclerViewAdapter(this, donors, new DonorRecyclerViewClickListener() {
            @Override
            public void onClick(Donor donor) {
                gotoPreview(donor);
            }
        });
        resultList.setAdapter(adapter);
        cardView.setVisibility(View.GONE);
    }

    private void switchScreenState() {
        if(donors.size() == 0){
            clInfo.setVisibility(View.VISIBLE);
            if(txtFname.getText().length() == 0 && txtLname.getText().length() == 0){
                info.setText("Start by entering the first name or last name of the donor in the fields above.");
            }else if(txtFname.getText().length() != 0 || txtLname.getText().length() != 0){
                info.setText("Donor information was not found.");
            }
        }else{
            clInfo.setVisibility(View.GONE);
            resultList.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.donor_verifier_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                if(cardView.getVisibility() == View.VISIBLE){
                    cardView.setVisibility(View.GONE);
                }else{
                    cardView.setVisibility(View.VISIBLE);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

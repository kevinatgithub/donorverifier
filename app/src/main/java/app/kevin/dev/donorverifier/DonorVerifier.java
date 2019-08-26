package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.adapters.DonorAdapter;
import app.kevin.dev.donorverifier.adapters.DonorRecyclerViewAdapter;
import app.kevin.dev.donorverifier.adapters.DonorRecyclerViewClickListener;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.Donor;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

public class DonorVerifier extends AppCompatActivity {

//    ListView resultList;
    RecyclerView resultList;
    RecyclerView.LayoutManager layoutManager;
    Button btnSearch;
    TextInputLayout tilFname;
    TextInputLayout tilLname;
    EditText txtFname;
    EditText txtLname;
    Realm realm;
    ConstraintLayout clInfo;
    TextView info;

    ArrayList<Donor> donors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_verifier);

        AppDrawerItemClickListener.prepareAppDrawer(this);
        realm = UserFn.getRealmInstance(this);

        resultList = findViewById(R.id.donorList);
        resultList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        resultList.setLayoutManager(layoutManager);

//        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Donor donor = (Donor) resultList.getItemAtPosition(i);
//                gotoPreview(donor);
//            }
//        });
        btnSearch = findViewById(R.id.btnSearch);
        tilFname = findViewById(R.id.tilFname);
        tilLname = findViewById(R.id.tilLname);
        txtFname = findViewById(R.id.txtFname);
        txtLname = findViewById(R.id.txtLname);
        clInfo = findViewById(R.id.cv_info);
        info = findViewById(R.id.info);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performSearch();
            }
        });

        switchScreenState();
    }

    private void gotoPreview(Donor donor) {
        Intent intent = new Intent(this,DonorPreview.class);
        intent.putExtra("seqno",donor.getSeqno());
        startActivity(intent);
    }

    private void performSearch() {
        String fname = txtFname.getText().toString();
        String lname = txtLname.getText().toString();

        RealmQuery query = realm.where(Donor.class);

        if(fname.length()>0){
            query.contains("fname",fname, Case.INSENSITIVE);
        }

        if(lname.length()>0){
            query.contains("lname",lname, Case.INSENSITIVE);
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
}

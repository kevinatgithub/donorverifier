package app.kevin.dev.donorverifier;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Upload extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        AppDrawerItemClickListener.prepareAppDrawer(this);
    }
}

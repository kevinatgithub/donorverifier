package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import app.kevin.dev.donorverifier.libs.Session;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkIfLoggedIn();
    }

    private void checkIfLoggedIn() {
        String str = Session.get(this,"user",null);
        if(str != null){
            Intent intent = new Intent(getApplicationContext(),News.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
        }
        finish();
    }
}

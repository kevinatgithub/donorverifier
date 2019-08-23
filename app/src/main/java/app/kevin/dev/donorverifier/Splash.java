package app.kevin.dev.donorverifier;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;

import org.json.JSONObject;

import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.ApiTest;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.ApiErrorCallback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.User;
import app.kevin.dev.donorverifier.models.api_response.LoginResponse;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

//        CallbackWithResponse response = new CallbackWithResponse() {
//            @Override
//            public void execute(@Nullable JSONObject response) {
//                if(response != null){
//                    LoginResponse loginResponse = UserFn.gson.fromJson(response.toString(),LoginResponse.class);
//                    UserFn.log(UserFn.gson.toJson(loginResponse.getUser()));
//                }
//            }
//        };
//
//        ApiTest.login(this, response,"11001","11001");

//        String URL = UserFn.url(UserFn.API_CITIES);
//        Api.call(this, URL, new CallbackWithResponse() {
//            @Override
//            public void execute(@Nullable JSONObject response) {
//                UserFn.log(response.toString());
//            }
//        });

//        String URL = UserFn.url(UserFn.API_BARANGAYS);
//        URL = URL.replace("{regcode}",UserFn.urlEncode("01"));
//        Api.call(this, URL, new CallbackWithResponse() {
//            @Override
//            public void execute(@Nullable JSONObject response) {
//                UserFn.log(response.toString());
//            }
//        });

//        String URL = UserFn.url(UserFn.API_NEWS);
//        URL = URL.replace("{max}","0");
//
//        Api.call(this, URL, new CallbackWithResponse() {
//            @Override
//            public void execute(@Nullable JSONObject response) {
//                UserFn.log(response.toString());
//            }
//        });

        String URL = UserFn.url(UserFn.API_GET_UPDATE);
        URL = URL.replace("{regcode}",UserFn.urlEncode("01"));
        URL = URL.replace("{last}",UserFn.urlEncode("2017-05-02"));
        Api.call(this, URL, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                UserFn.log(response.toString());
            }
        });

    }
}

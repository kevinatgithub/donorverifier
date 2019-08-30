package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.ConnectivityManager;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.ApiErrorCallback;
import app.kevin.dev.donorverifier.models.Callback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.User;
import app.kevin.dev.donorverifier.models.api_response.LoginResponse;

public class Login extends AppCompatActivity {

    EditText txtUsername,txtPassword, txtPassword2;
    TextView txtInfo,txtInfo2,txtAccount;
    Button btnLogin, btnLogin2, btnForget;
    ProgressBar pbLoading;
    CardView form1, form2;
    User rememberedAccount = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        form1 = findViewById(R.id.form);
        form2 = findViewById(R.id.form2);
        txtInfo = findViewById(R.id.txtInfo);
        txtInfo2 = findViewById(R.id.txtInfo2);
        txtAccount = findViewById(R.id.account);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtPassword2 = findViewById(R.id.txtPassword2);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin2 = findViewById(R.id.btnLogin2);
        btnForget = findViewById(R.id.btnForget);
        pbLoading = findViewById(R.id.pbLoading);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
        btnLogin2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateLoginAgain();
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserFn.confirm(Login.this, "Remove account", "This will remove current account details, and signing-in will require internet connection.", new Callback() {
                    @Override
                    public void execute() {
                        Session.delete(Login.this,"remembered_account");
                        form1.setVisibility(View.VISIBLE);
                        form2.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        switchDisplay();

    }

    private void switchDisplay() {
        String remembered_account_str = Session.get(this,"remembered_account",null);
        if(remembered_account_str != null){
            User user = UserFn.gson.fromJson(remembered_account_str,User.class);
            rememberedAccount = user;
            txtAccount.setText(UserFn.convertToTitleCaseIteratingChars(user.getUser_fname() + " " + user.getUser_mname() + " " + user.getUser_lname()));

            form1.setVisibility(View.GONE);
            form2.setVisibility(View.VISIBLE);
        }
    }

    private void validateLoginAgain() {
        if(txtPassword2.getText().length() == 0){
            txtInfo2.setText("Please enter password");
            txtInfo2.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }else{
            txtInfo2.setText("");
            if(txtPassword2.getText().toString().equals(rememberedAccount.getPassword())){
                Intent intent = new Intent(this,DonorVerifier.class);
                startActivity(intent);
                finish();
            }else{
                txtInfo2.setText("Login failed, please check your username/password.");
                txtInfo2.setTextColor(getResources().getColor(android.R.color.holo_red_light));
            }
        }
    }

    private void validateForm() {
        txtInfo.setText("");
        txtInfo.setTextColor(getResources().getColor(android.R.color.black));

        String error = "";
        if(txtUsername.getText().toString().length() == 0){
            error = "Please provide a valid username";
        }
        if(txtPassword.getText().toString().length() == 0){
            error = "Please enter your password";
        }
        if(txtUsername.getText().toString().length() > 0 && txtPassword.getText().toString().length() > 0){
            txtInfo.setText("Please wait, logging in..");
            txtInfo.setTextColor(getResources().getColor(android.R.color.holo_blue_light));
            attemptLogin(txtUsername.getText().toString(),txtPassword.getText().toString());
        }else{
            txtInfo.setText(error);
            txtInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
        }
    }

    private void attemptLogin(String username, String password) {
        String url = UserFn.url(UserFn.API_LOGIN);
        JSONObject params = new JSONObject();
        params = UserFn.addParam(params,"username",username);
        params = UserFn.addParam(params,"password",password);

        toggleLoading(true);
        Api.call(this, url, Request.Method.POST, params, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                toggleLoading(false);
                LoginResponse loginResponse = UserFn.gson.fromJson(response.toString(), LoginResponse.class);
                if (loginResponse.getStatus().equals("ok")) {
                    processLogin(loginResponse.getUser());
                } else {
                    txtInfo.setText("Login failed, please check your username/password.");
                    txtInfo.setTextColor(getResources().getColor(android.R.color.holo_red_light));
                    txtUsername.setText("");
                    txtPassword.setText("");
                }
            }
        }, new ApiErrorCallback() {
            @Override
            public void uponError(VolleyError error) {

            }
        });
    }

    private void processLogin(User user) {
        user.setPassword(txtPassword.getText().toString());
        Session.set(getApplicationContext(),"user", UserFn.gson.toJson(user));
        Session.set(getApplicationContext(),"remembered_account", UserFn.gson.toJson(user));
        Intent intent = new Intent(this,DonorVerifier.class);
        startActivity(intent);
        finish();
    }

    private void toggleLoading(boolean state){
        if(state){
            pbLoading.setVisibility(View.VISIBLE);
            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);
            btnLogin.setEnabled(false);

        }else{
            txtInfo.setText("");
            pbLoading.setVisibility(View.INVISIBLE);
            txtUsername.setEnabled(true);
            txtPassword.setEnabled(true);
            btnLogin.setEnabled(true);

        }
    }
}

package app.kevin.dev.donorverifier;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONObject;

import app.kevin.dev.donorverifier.libs.Api;
import app.kevin.dev.donorverifier.libs.Session;
import app.kevin.dev.donorverifier.libs.UserFn;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.User;
import app.kevin.dev.donorverifier.models.api_response.LoginResponse;

public class Login extends AppCompatActivity {

    TextInputLayout tilUsername;
    TextInputLayout tilPassword;
    EditText txtUsername;
    EditText txtPassword;
    Button btnLogin;
    ProgressBar pbLoading;
    TextView lblLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        pbLoading = findViewById(R.id.pbLoading);
        lblLoading = findViewById(R.id.lblLoading);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateForm();
            }
        });
    }

    private void validateForm() {
        tilUsername.setError(null);
        tilPassword.setError(null);

        if(txtUsername.getText().toString().length() == 0){
            tilUsername.setError("Please provide a valid username");
        }
        if(txtPassword.getText().toString().length() == 0){
            tilPassword.setError("Please enter your password");
        }
        if(txtUsername.getText().toString().length() > 0 && txtPassword.getText().toString().length() > 0){
            attemptLogin(txtUsername.getText().toString(),txtPassword.getText().toString());
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
                if(loginResponse.getStatus().equals("ok")){
                    processLogin(loginResponse.getUser());
                }else{
                    tilPassword.setError("Login failed, please check your username/password.");
                    txtUsername.setText("");
                    txtPassword.setText("");
                }
            }
        });
    }

    private void processLogin(User user) {
        Session.set(getApplicationContext(),"user", UserFn.gson.toJson(user));
        Intent intent = new Intent(this,News.class);
        startActivity(intent);
        Toast.makeText(this, "Welcome " + user.getUser_fname() + " " + user.getUser_lname(), Toast.LENGTH_SHORT).show();
        finish();
    }

    private void toggleLoading(boolean state){
        if(state){
            pbLoading.setVisibility(View.VISIBLE);
            lblLoading.setVisibility(View.VISIBLE);
            txtUsername.setEnabled(false);
            txtPassword.setEnabled(false);
            btnLogin.setEnabled(false);

        }else{
            pbLoading.setVisibility(View.INVISIBLE);
            lblLoading.setVisibility(View.INVISIBLE);
            txtUsername.setEnabled(true);
            txtPassword.setEnabled(true);
            btnLogin.setEnabled(true);

        }
    }
}

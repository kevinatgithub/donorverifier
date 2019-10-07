package doh.nvbsp.nbbnets.donorverifier;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONObject;

import doh.nvbsp.nbbnets.donorverifier.libs.Api;
import doh.nvbsp.nbbnets.donorverifier.libs.UserFn;
import doh.nvbsp.nbbnets.donorverifier.models.CallbackWithResponse;

public class CheckUpdates extends AppCompatActivity {

    TextView UpdatedInfo, OutdatedInfo, ServerInfo;
    String outdatedInfo, updatedInfo, serverInfo;
    Button UpdateApp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_updates);

        AppDrawerItemClickListener.prepareAppDrawer(this);

        UpdatedInfo = findViewById(R.id.txtUpdatedInfo);
        OutdatedInfo = findViewById(R.id.txtOutdatedInfo);
        ServerInfo = findViewById(R.id.txtServerInfo);
        UpdateApp = findViewById(R.id.cmdUpdateApp);

        UpdatedInfo.setVisibility(View.GONE);
        OutdatedInfo.setVisibility(View.GONE);
        UpdateApp.setVisibility(View.GONE);

        updatedInfo = "You have the latest version of Donor Verifier app";
        outdatedInfo = "Your application is outdated. \nPlease click the button to update the application";
        serverInfo = "Latest released version is: Version ";

        UpdatedInfo.setText(updatedInfo);
        OutdatedInfo.setText(outdatedInfo);

        checkUpdate();

        UpdateApp.setOnClickListener(new goToLatestVersion());

    }

    private void checkUpdate(){
        String url = UserFn.url(UserFn.API_CHECK_UPDATE);

        Api.call(this, url, new CallbackWithResponse() {
            @Override
            public void execute(@Nullable JSONObject response) {
                VersionCheckResponse vcr = UserFn.gson.fromJson(response.toString(),VersionCheckResponse.class);

                int responseVersion = Integer.parseInt(vcr.getVersion());

                String currentVersionStr = getResources().getString(R.string.application_version);

                int currentVersion = Integer.parseInt(currentVersionStr);

                if(responseVersion > currentVersion){
                    // TODO: 02/10/2019  Show the Needs update message
                    OutdatedInfo.setVisibility(View.VISIBLE);
                    UpdateApp.setVisibility(View.VISIBLE);

                    ServerInfo.setText(serverInfo + vcr.getVersion());
                    
                }else{
                    // TODO: 02/10/2019 No update available
                    UpdatedInfo.setVisibility(View.VISIBLE);

                    ServerInfo.setText(serverInfo + vcr.getVersion());
                }
            }
        });
    }

    private class VersionCheckResponse{
        private String version;

        public String getVersion() {
            return version;
        }
    }


     // GO TO LATEST VERSION DOWNLOAD PAGE
    class goToLatestVersion implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            // call a dialog box informing that the user will be redirected
            proceedDownloadLatest(CheckUpdates.this).show();
        }
    }

     // DIALOG BOX
     public AlertDialog.Builder proceedDownloadLatest(Context c) {
         AlertDialog.Builder builder = new AlertDialog.Builder(c);
         builder.setTitle("Download Latest Version");
         builder.setMessage("You are about to be redirected to the official download page of DonorID app, click 'CONFIRM' to proceed.");

         builder.setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {

             @Override
             public void onClick(DialogInterface dialog, int which) {
                 // download url
                 String downloadUrl = "http://test.nbbnets.net/app";
                 // set url
                 Uri uri = Uri.parse(downloadUrl);
                 Intent iGoToLatestVerion = new Intent(Intent.ACTION_VIEW, uri);
                 startActivity(iGoToLatestVerion);
             }
         });

         builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int which) {
                 return;
             }
         });
         return builder;
     }
     // GO TO LATEST VERSION DOWNLOAD PAGE











    

}

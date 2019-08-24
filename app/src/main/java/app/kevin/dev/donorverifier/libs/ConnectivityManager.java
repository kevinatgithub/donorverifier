package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.content.Context;
import android.net.NetworkInfo;
import android.widget.Toast;

import app.kevin.dev.donorverifier.NetworkFailDialog;
import app.kevin.dev.donorverifier.models.Callback;

public class ConnectivityManager {
    public static void checkConnection(final Activity activity, final Callback CALLBACK){

        android.net.ConnectivityManager conMgr =  (android.net.ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
        if (netInfo == null){
            NetworkFailDialog.show(activity, new Callback() {
                @Override
                public void execute() {
                    checkConnection(activity,CALLBACK);
                }
            });
        }else{
            CALLBACK.execute();
        }
    }
}

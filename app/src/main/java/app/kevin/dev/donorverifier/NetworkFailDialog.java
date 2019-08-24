package app.kevin.dev.donorverifier;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import app.kevin.dev.donorverifier.models.Callback;

public class NetworkFailDialog {
    final static private String CONNECTION_ERROR = "Can't Connect";

    public static void show(final Activity activity, final Callback CALLBACK){
        LayoutInflater inflater = LayoutInflater.from(activity);
        final AlertDialog DIALOG = new AlertDialog.Builder(activity).create();
        DIALOG.setTitle(CONNECTION_ERROR);
        View customView = inflater.inflate(R.layout.network_fail,null);
        DIALOG.setView(customView);
        DIALOG.setCancelable(false);
        DIALOG.setButton(AlertDialog.BUTTON_POSITIVE, "TRY AGAIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DIALOG.dismiss();
                CALLBACK.execute();
            }
        });

        DIALOG.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        });

        DIALOG.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface dialogInterface) {
                DIALOG.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorAccent);
                DIALOG.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimary);
            }
        });
        DIALOG.show();
    }
}

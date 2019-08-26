package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.kevin.dev.donorverifier.Download;
import app.kevin.dev.donorverifier.models.Callback;
import app.kevin.dev.donorverifier.models.DownloadState;
import app.kevin.dev.donorverifier.models.Region;
import io.realm.Realm;
import io.realm.RealmConfiguration;

public class UserFn {

    public static Gson gson = new Gson();
    public static int POST_METHOD = Request.Method.POST;

    public static String API_SERVER = "http://test.nbbnets.net/pbc/public/api/app/";
    public static String API_LOGIN = "login";
    public static String API_DONOR_PHOTO = "photo/{seqno}";
//    public static String API_CHECK_UPDATE_COUNT = "checkupdates/count/{last}";
//    public static String API_CHECK_UPDATE = "checkupdates/{rows}/{last}";
    public static String API_GET_UPDATE_COUNT = "getUpdateCount/{regcode}/{last}";
    public static String API_GET_UPDATE = "getUpdate/{regcode}/{last}";
    public static String API_GET_UPDATE_CHUNK = "getUpdateChunk/{regcode}/{start}/{size}";
    public static String API_REGIONS = "regions";
    public static String API_PROVINCES = "provinces";
    public static String API_CITIES = "cities";
    public static String API_BARANGAYS = "barangays/{regcode}";
    public static String API_NEWS = "news";
    public static String API_UPLOAD = "upload";
    public static String API_UPLOAD_PHOTO = "uploadphoto";

    public static String urlEncode(String str){
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String url(String end_point){
        return API_SERVER + end_point;
    }

    public static void log(String message){
        if(message != null)
            Log.d("LOG MESSAGE",message);
        else
            Log.e("ERROR_SHOWING_MESSAGE","no message provided");
    }

    public static void clearDB(Realm realm) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.deleteAll();
            }
        });
    }

    public static JSONObject addParam(JSONObject request_body, String key, String val){
        try {
            request_body.put(key,UserFn.urlEncode(val));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request_body;
    }

    public static void cantConnect(Context context){
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Can't connect to server");
        alertDialog.setMessage("Please check your connection and try again");
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alertDialog.show();
    }

    public static void confirm(Context context, String title, String message, final Callback onYes){
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        onYes.execute();
                    }})
                .setNegativeButton(android.R.string.no, null).show();
    }

    public static Realm getRealmInstance(Context context){
        Realm.init(context);
        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getDefaultInstance();
    }

    public static DownloadState getDownloadState(Context context){
        String str = Session.get(context,"downloadState",null);
        DownloadState state = new DownloadState();
        if(str != null){
            state = gson.fromJson(str, DownloadState.class);
        }

        return state;
    }

    public static String convertToTitleCaseIteratingChars(String text) {
        if (text == null || text.isEmpty()) {
            return text;
        }

        StringBuilder converted = new StringBuilder();

        boolean convertNext = true;
        for (char ch : text.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }
            converted.append(ch);
        }

        return converted.toString();
    }
}

package doh.nvbsp.nbbnets.donorverifier.libs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.getkeepsafe.taptargetview.TapTarget;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import doh.nvbsp.nbbnets.donorverifier.R;
import doh.nvbsp.nbbnets.donorverifier.models.Callback;
import doh.nvbsp.nbbnets.donorverifier.models.DownloadState;
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
    public static String API_GET_DEFERRED_COUNT = "getBlockCount/{last}";
    public static String API_GET_UPDATE = "getUpdate/{regcode}/{last}";
    public static String API_GET_UPDATE_CHUNK = "getUpdateChunk/{regcode}/{last}/{start}/{size}";
    public static String API_GET_DEFERRED_CHUNK = "getBlockChunk/{last}/{start}/{size}";
    public static String API_GET_TOTAL = "getTotal";
    public static String API_GET_TOTAL_IN_REGION = "getTotal/{regcode}";
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
//        new AlertDialog.Builder(context)
//                .setTitle(title)
//                .setMessage(message)
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//
//                    public void onClick(DialogInterface dialog, int whichButton) {
//                        onYes.execute();
//                    }})
//                .setNegativeButton(android.R.string.no, null).show();

        LayoutInflater inflater = LayoutInflater.from(context);
        final android.support.v7.app.AlertDialog DIALOG = new android.support.v7.app.AlertDialog.Builder(context).create();
        DIALOG.setTitle(title);
        DIALOG.setMessage(message);
        View customView = inflater.inflate(android.R.layout.select_dialog_item,null);
        DIALOG.setView(customView);
        DIALOG.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DIALOG.dismiss();
                onYes.execute();
            }
        });

        DIALOG.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        DIALOG.setOnShowListener(new DialogInterface.OnShowListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onShow(DialogInterface dialogInterface) {
                DIALOG.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.colorAccent);
                DIALOG.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(R.color.colorPrimary);
            }
        });
        DIALOG.show();
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

    public static void attachDatePicker(final Activity activity, int id, final DateSelectedListner selectedListner){
        final Calendar myCalendar = Calendar.getInstance();

        EditText edittext= activity.findViewById(id);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String myFormat = "MMMM dd, yyyy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                selectedListner.onSet(sdf.format(myCalendar.getTime()));
            }

        };

        edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(activity, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public interface DateSelectedListner{
        void onSet(String date);
    }

    public static String getCurrentDateString(){
        Date c = Calendar.getInstance().getTime();
        return new SimpleDateFormat("yyyyy-MM-dd").format(c);
    }

    public static TapTarget buildTapTargetToolbar(final Activity activity, final int id,final int title, final int message){
        Toolbar toolbar = activity.findViewById(R.id.app_bar);
        TapTarget target = TapTarget.forToolbarMenuItem(toolbar,id,activity.getResources().getString(title),activity.getResources().getString(message));
        return target;
    }

    public static TapTarget buildTapTargetNavigationIcon(final Activity activity, final int title, final int message){
        Toolbar toolbar = activity.findViewById(R.id.app_bar);
        TapTarget target = TapTarget.forToolbarNavigationIcon(toolbar,activity.getResources().getString(title),activity.getResources().getString(message));
        return target;
    }

    public static TapTarget buildTapTarget(final Activity activity, final int id,final int title, final int message){
        TapTarget target = TapTarget.forView(activity.findViewById(id),activity.getResources().getString(title),activity.getResources().getString(message));
        return target;
    }

}

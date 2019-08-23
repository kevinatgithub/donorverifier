package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.Region;
import io.realm.Realm;

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
    public static String API_REGIONS = "regions";
    public static String API_PROVINCES = "provinces";
    public static String API_CITIES = "cities";
    public static String API_BARANGAYS = "barangays/{regcode}";
    public static String API_NEWS = "news/{max}";
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
        Log.d("LOG MESSAGE",message);
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
}

package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.util.Log;

import com.android.volley.Request;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.Region;

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

}

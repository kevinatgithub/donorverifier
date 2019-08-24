package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import app.kevin.dev.donorverifier.models.ApiErrorCallback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;

public class Api {
    private static Activity activity;
    private static RequestQueue requestQueue;

    private static void init(Activity _activity){
        activity = _activity;

        if(requestQueue == null){
            requestQueue = Volley.newRequestQueue(activity);
        }

    }

    public static void call(final Activity ACTIVITY,final String URL,@Nullable final CallbackWithResponse CALLBACK){
        init(ACTIVITY);
        requestQueue.add(
                new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (CALLBACK != null){
                            CALLBACK.execute(response);
                        }
                    }
                }, errorListener(null,ACTIVITY))
        );
    }

    public static void call(final Activity ACTIVITY,final String URL,@Nullable final CallbackWithResponse CALLBACK, @Nullable final ApiErrorCallback ERROR_CALLBACK){
        init(ACTIVITY);
        requestQueue.add(
                new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (CALLBACK != null){
                            CALLBACK.execute(response);
                        }
                    }
                }, errorListener(ERROR_CALLBACK, ACTIVITY))
        );
    }

    public static void call(final Activity ACTIVITY,final String URL,final int METHOD,final JSONObject REQUEST_BODY,@Nullable final CallbackWithResponse CALLBACK, @Nullable final ApiErrorCallback ERROR_CALLBACK){
        init(ACTIVITY);
        requestQueue.add(
                new JsonObjectRequest(METHOD, URL, REQUEST_BODY, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (CALLBACK != null){
                            CALLBACK.execute(response);
                        }
                    }
                }, errorListener(ERROR_CALLBACK, ACTIVITY))
        );
    }

    public static void call(final Activity ACTIVITY,final String URL,final int METHOD,final JSONObject REQUEST_BODY,@Nullable final CallbackWithResponse CALLBACK){
        init(ACTIVITY);
        requestQueue.add(
                new JsonObjectRequest(METHOD, URL, REQUEST_BODY, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        if (CALLBACK != null){
                            CALLBACK.execute(response);
                        }
                    }
                }, errorListener(null, ACTIVITY))
        );
    }

    private static Response.ErrorListener errorListener(final ApiErrorCallback ERROR_CALLBACK, final Activity ACTIVITY){
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(ERROR_CALLBACK != null){
                    ERROR_CALLBACK.uponError(error);
                }else{
                    UserFn.cantConnect(ACTIVITY);
                    Log.e("CANT_CONNECT",error.getMessage());
                }
            }
        };
    }
}

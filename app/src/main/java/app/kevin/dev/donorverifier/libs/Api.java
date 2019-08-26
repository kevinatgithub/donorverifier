package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import app.kevin.dev.donorverifier.models.ApiErrorCallback;
import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.api_response.CallbackWithStringResponse;

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
//                    Log.e("CANT_CONNECT",error.getMessage());
                }
            }
        };
    }

    public static JSONObject callWait(String URL){
        RequestFuture<JSONObject> future = RequestFuture.newFuture();
        JsonObjectRequest request = new JsonObjectRequest(URL, new JSONObject(), future, future);
        requestQueue.add(request);

        try {
            JSONObject response = future.get(); // this will block
            return response;
        } catch (InterruptedException e) {
            Log.e("ERROR",e.getMessage());
            // exception handling
        } catch (ExecutionException e) {
            Log.e("ERROR",e.getMessage());
            // exception handling
        }

        return null;
    }

    public static void getString(Activity activity, String url, final CallbackWithStringResponse callback) {
        StringRequest strReq = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                callback.execute(response);
            }
        },errorListener(new ApiErrorCallback() {
            @Override
            public void uponError(VolleyError error) {
                if(error != null)
                    Log.e("error",error.getMessage());
            }
        }, activity));

        requestQueue.add(strReq);
    }
}

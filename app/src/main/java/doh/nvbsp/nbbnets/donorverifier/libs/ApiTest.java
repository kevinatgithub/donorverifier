package doh.nvbsp.nbbnets.donorverifier.libs;

import android.app.Activity;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import doh.nvbsp.nbbnets.donorverifier.models.ApiErrorCallback;
import doh.nvbsp.nbbnets.donorverifier.models.CallbackWithResponse;

public class ApiTest {

    public static void login(final Activity activity, CallbackWithResponse CALLBACK, final String username, final String password){
        JSONObject request_body = new JSONObject();
        request_body = addParam(request_body,"username",username);
        request_body = addParam(request_body,"password",password);
        String url = UserFn.API_SERVER + UserFn.API_LOGIN;
        Api.call(activity, url, UserFn.POST_METHOD, request_body, CALLBACK, new ApiErrorCallback() {
            @Override
            public void uponError(VolleyError error) {
                UserFn.log(error.getMessage());
            }
        });
    }

    public static void provinces(Activity activity, CallbackWithResponse CALLBACK){
        String URL = UserFn.url(UserFn.API_PROVINCES);
        Api.call(activity,URL,CALLBACK);
    }

    private static JSONObject addParam(JSONObject request_body,String key, String val){
        try {
            request_body.put(key,UserFn.urlEncode(val));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return request_body;
    }
}

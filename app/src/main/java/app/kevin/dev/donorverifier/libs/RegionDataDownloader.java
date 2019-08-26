package app.kevin.dev.donorverifier.libs;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.annotation.Nullable;;import org.json.JSONObject;

import app.kevin.dev.donorverifier.models.CallbackWithResponse;
import app.kevin.dev.donorverifier.models.api_response.UpdateResponse;

public class RegionDataDownloader extends AsyncTask<String,Void,JSONObject> {

    private static final Object PER_BATCH_COUNT = 100;
    Activity activity;
    int start;
    Callback callback;
    JSONObject apiResponse;

    public RegionDataDownloader(Activity activity, int start, Callback callback) {
        this.activity = activity;
        this.start = start;
        this.callback = callback;
    }

    @Override
    protected JSONObject doInBackground(String... strings) {
        String url = UserFn.url(UserFn.API_GET_UPDATE_CHUNK);
        String regcode = strings[0];
        url = url.replace("{regcode}", UserFn.urlEncode(regcode));
        String s = String.valueOf(start * (int) PER_BATCH_COUNT);
        url = url.replace("{start}", UserFn.urlEncode(s));
        url = url.replace("{size}", UserFn.urlEncode(String.valueOf((int) PER_BATCH_COUNT)));

//        Api.call(activity, url, new CallbackWithResponse() {
//            @Override
//            public void execute(@Nullable JSONObject response) {
//                apiResponse = UserFn.gson.fromJson(response.toString(), UpdateResponse.class);
//            }
//        });

        return Api.fetchModules(activity,url);

    }

    @Override
    protected void onPostExecute(JSONObject data) {

        super.onPostExecute(data);
        callback.execute(data);
    }

    public interface Callback{
        void execute(JSONObject responseData);
    }
}

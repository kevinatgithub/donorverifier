package app.kevin.dev.donorverifier.models;

import android.support.annotation.Nullable;

import org.json.JSONObject;

public interface CallbackWithResponse {
    void execute(@Nullable JSONObject response);
}

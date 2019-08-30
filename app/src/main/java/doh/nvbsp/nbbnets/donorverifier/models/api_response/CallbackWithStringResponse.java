package doh.nvbsp.nbbnets.donorverifier.models.api_response;

import android.support.annotation.Nullable;

public interface CallbackWithStringResponse {
    void execute(@Nullable String response);
}

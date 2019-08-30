package doh.nvbsp.nbbnets.donorverifier.models;

import com.android.volley.VolleyError;

public interface ApiErrorCallback {
    void uponError(VolleyError error);
}

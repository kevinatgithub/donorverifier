package app.kevin.dev.donorverifier.models.api_response;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.Barangay;

public class BarangaysResponse {
    private String status;
    private ArrayList<Barangay> data;

    public BarangaysResponse(String status, ArrayList<Barangay> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Barangay> getData() {
        return data;
    }
}

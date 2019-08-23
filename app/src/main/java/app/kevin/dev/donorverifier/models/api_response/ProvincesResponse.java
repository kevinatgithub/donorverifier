package app.kevin.dev.donorverifier.models.api_response;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.Province;

public class ProvincesResponse {
    private String status;
    private ArrayList<Province> data;

    public ProvincesResponse(String status, ArrayList<Province> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Province> getData() {
        return data;
    }
}

package app.kevin.dev.donorverifier.models.api_response;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.City;

public class CityResponse {
    private String status;
    private ArrayList<City> data;

    public CityResponse(String status, ArrayList<City> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<City> getData() {
        return data;
    }
}

package app.kevin.dev.donorverifier.models.api_response;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.Donor;

public class UpdateResponse {

    private String status;
    private ArrayList<Donor> data;

    public UpdateResponse(String status, ArrayList<Donor> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<Donor> getData() {
        return data;
    }

}

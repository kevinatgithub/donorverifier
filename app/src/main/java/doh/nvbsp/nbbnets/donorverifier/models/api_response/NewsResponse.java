package doh.nvbsp.nbbnets.donorverifier.models.api_response;

import java.util.ArrayList;

import doh.nvbsp.nbbnets.donorverifier.models.News;

public class NewsResponse {
    private String status;
    private ArrayList<News> data;

    public NewsResponse(String status, ArrayList<News> data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public ArrayList<News> getData() {
        return data;
    }
}

package app.kevin.dev.donorverifier.models.api_response;

import java.util.ArrayList;

import app.kevin.dev.donorverifier.models.News;

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

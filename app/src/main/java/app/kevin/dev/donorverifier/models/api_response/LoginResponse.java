package app.kevin.dev.donorverifier.models.api_response;

import app.kevin.dev.donorverifier.models.User;

public class LoginResponse {
    private String status;
    private User user;

    public LoginResponse(String status, User user) {
        this.status = status;
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }
}

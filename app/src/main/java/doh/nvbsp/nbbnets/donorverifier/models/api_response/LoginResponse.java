package doh.nvbsp.nbbnets.donorverifier.models.api_response;

import doh.nvbsp.nbbnets.donorverifier.models.User;

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

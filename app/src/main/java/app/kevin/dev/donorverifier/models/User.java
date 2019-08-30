package app.kevin.dev.donorverifier.models;

public class User {
    private String user_id;
    private int ulevel;
    private String user_fname;
    private String user_mname;
    private String user_lname;
    private String facility_cd;
    private String password;

    public User(String user_id, int ulevel, String user_fname, String user_mname, String user_lname, String facility_cd) {
        this.user_id = user_id;
        this.ulevel = ulevel;
        this.user_fname = user_fname;
        this.user_mname = user_mname;
        this.user_lname = user_lname;
        this.facility_cd = facility_cd;
    }

    public String getUser_id() {
        return user_id;
    }

    public int getUlevel() {
        return ulevel;
    }

    public String getUser_fname() {
        return user_fname;
    }

    public String getUser_mname() {
        return user_mname;
    }

    public String getUser_lname() {
        return user_lname;
    }

    public String getFacility_cd() {
        return facility_cd;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

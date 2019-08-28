package app.kevin.dev.donorverifier.models;

import io.realm.RealmObject;

public class Donation extends RealmObject {
    private String donation_id;
    private String donation_dt;
    private String facility;

    public String getDonation_id() {
        return donation_id;
    }

    public void setDonation_id(String donation_id) {
        this.donation_id = donation_id;
    }

    public String getDonation_dt() {
        return donation_dt;
    }

    public void setDonation_dt(String donation_dt) {
        this.donation_dt = donation_dt;
    }

    public String getFacility() {
        return facility;
    }

    public void setFacility(String facility) {
        this.facility = facility;
    }
}

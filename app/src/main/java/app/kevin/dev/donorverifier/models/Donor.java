package app.kevin.dev.donorverifier.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Donor extends RealmObject {
    @PrimaryKey
    private String seqno;
    private String donor_photo;
    private String barcode;
    private String donor_id;
    private String fname;
    private String mname;
    private String lname;
    private String gender;
    private String bdate;
    private String home_no_st_blk;
    private String region;
    private String province;
    private String city;
    private String barangay;
    private String donation_stat;

    private RealmList<Donation> donations;

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
    }

    public String getDonor_photo() {
        return donor_photo;
    }

    public void setDonor_photo(String donor_photo) {
        this.donor_photo = donor_photo;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getDonor_id() {
        return donor_id;
    }

    public void setDonor_id(String donor_id) {
        this.donor_id = donor_id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getHome_no_st_blk() {
        return home_no_st_blk;
    }

    public void setHome_no_st_blk(String home_no_st_blk) {
        this.home_no_st_blk = home_no_st_blk;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getBarangay() {
        return barangay;
    }

    public void setBarangay(String barangay) {
        this.barangay = barangay;
    }

    public String getDonation_stat() {
        return donation_stat;
    }

    public void setDonation_stat(String donation_stat) {
        this.donation_stat = donation_stat;
    }

    public RealmList<Donation> getDonations() {
        return donations;
    }

    public void setDonations(RealmList<Donation> donations) {
        this.donations = donations;
    }
}

package app.kevin.dev.donorverifier.models;

public class Donor {
    private String seqno;
    private String donor_id;
    private String fname;
    private String mname;
    private String lname;
    private String gender;
    private String bdate;
    private String home_no_st_blk;
    private String home_brgy;
    private String home_prov;
    private String home_city;
    private String home_region;
    private String donation_stat;

    public Donor(String seqno, String donor_id, String fname, String mname, String lname, String gender, String bdate, String home_no_st_blk, String home_brgy, String home_prov, String home_city, String home_region, String donation_stat) {
        this.seqno = seqno;
        this.donor_id = donor_id;
        this.fname = fname;
        this.mname = mname;
        this.lname = lname;
        this.gender = gender;
        this.bdate = bdate;
        this.home_no_st_blk = home_no_st_blk;
        this.home_brgy = home_brgy;
        this.home_prov = home_prov;
        this.home_city = home_city;
        this.home_region = home_region;
        this.donation_stat = donation_stat;
    }

    public String getSeqno() {
        return seqno;
    }

    public void setSeqno(String seqno) {
        this.seqno = seqno;
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

    public String getHome_brgy() {
        return home_brgy;
    }

    public void setHome_brgy(String home_brgy) {
        this.home_brgy = home_brgy;
    }

    public String getHome_prov() {
        return home_prov;
    }

    public void setHome_prov(String home_prov) {
        this.home_prov = home_prov;
    }

    public String getHome_city() {
        return home_city;
    }

    public void setHome_city(String home_city) {
        this.home_city = home_city;
    }

    public String getHome_region() {
        return home_region;
    }

    public void setHome_region(String home_region) {
        this.home_region = home_region;
    }

    public String getDonation_stat() {
        return donation_stat;
    }

    public void setDonation_stat(String donation_stat) {
        this.donation_stat = donation_stat;
    }
}
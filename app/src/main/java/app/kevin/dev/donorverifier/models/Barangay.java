package app.kevin.dev.donorverifier.models;

public class Barangay {
    private String regcode;
    private String provcode;
    private String citycode;
    private String bgycode;
    private String bgyname;

    public Barangay(String regcode, String provcode, String citycode, String bgycode, String bgyname) {
        this.regcode = regcode;
        this.provcode = provcode;
        this.citycode = citycode;
        this.bgycode = bgycode;
        this.bgyname = bgyname;
    }

    public String getRegcode() {
        return regcode;
    }

    public String getProvcode() {
        return provcode;
    }

    public String getCitycode() {
        return citycode;
    }

    public String getBgycode() {
        return bgycode;
    }

    public String getBgyname() {
        return bgyname;
    }
}

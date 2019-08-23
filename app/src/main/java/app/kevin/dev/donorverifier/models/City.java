package app.kevin.dev.donorverifier.models;

public class City {
    private String regcode;
    private String provcode;
    private String citycode;
    private String cityname;

    public City(String regcode, String provcode, String citycode, String cityname) {
        this.regcode = regcode;
        this.provcode = provcode;
        this.citycode = citycode;
        this.cityname = cityname;
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

    public String getCityname() {
        return cityname;
    }
}

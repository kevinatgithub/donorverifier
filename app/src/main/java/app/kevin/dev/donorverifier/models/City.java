package app.kevin.dev.donorverifier.models;

import io.realm.RealmObject;

public class City extends RealmObject {
    private String regcode;
    private String provcode;
    private String citycode;
    private String cityname;

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

package app.kevin.dev.donorverifier.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Barangay extends RealmObject{
    private String regcode;
    private String provcode;
    private String citycode;
    @PrimaryKey
    private String bgycode;
    private String bgyname;

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

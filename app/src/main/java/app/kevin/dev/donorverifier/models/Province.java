package app.kevin.dev.donorverifier.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Province extends RealmObject {

    private String regcode;
    @PrimaryKey
    private String provcode;
    private String provname;


    public String getRegcode() {
        return regcode;
    }

    public String getProvcode() {
        return provcode;
    }

    public String getProvname() {
        return provname;
    }
}

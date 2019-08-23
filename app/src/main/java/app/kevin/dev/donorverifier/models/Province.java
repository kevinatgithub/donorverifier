package app.kevin.dev.donorverifier.models;

public class Province {
    private String regcode;
    private String provcode;
    private String provname;

    public Province(String regcode, String provcode, String provname) {
        this.regcode = regcode;
        this.provcode = provcode;
        this.provname = provname;
    }

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

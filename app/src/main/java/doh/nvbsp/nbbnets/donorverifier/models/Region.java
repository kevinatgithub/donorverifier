package doh.nvbsp.nbbnets.donorverifier.models;

import java.util.ArrayList;

public class Region {
    private String regcode;
    private String regname;
    private String dbname;

    public Region(String regcode, String regname, String dbname) {
        this.regcode = regcode;
        this.regname = regname;
        this.dbname = dbname;
    }

    public String getRegcode() {
        return regcode;
    }

    public String getRegname() {
        return regname;
    }

    public void setRegcode(String regcode) {
        this.regcode = regcode;
    }

    public void setRegname(String regname) {
        this.regname = regname;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public static ArrayList<Region> getRegions(){
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region("01","Region 1 Ilocos Region", "Region 1"));
        regions.add(new Region("02","Region 2 Cagayan Valley", "Region 2"));
        regions.add(new Region("03","Region 3 Central Luzon", "Region 3"));
        regions.add(new Region("04","Region 4A Southern Tagalog Mainland", "Region 4A"));
        regions.add(new Region("17","Region 4B MIMAROPA", "Region 4B"));
        regions.add(new Region("05","Region 5 Bicol Region", "Region 5"));
        regions.add(new Region("06","Region 6 Western Visayas", "Region 6"));
        regions.add(new Region("07","Region 7 Central Visayas", "Region 7"));
        regions.add(new Region("08","Region 8 Eastern Visayas", "Region 8"));
        regions.add(new Region("09","Region 9 Zamboanga Peninsula", "Region 9"));
        regions.add(new Region("10","Region 10 Northern Mindanao", "Region 10"));
        regions.add(new Region("11","Region 11 Davao Region", "Region 11"));
        regions.add(new Region("12","Region 12 SOCCSKSARGEN", "Region 12"));
        regions.add(new Region("13","National Capital Region", "NCR"));
        regions.add(new Region("14","Cordillera Administrative Region", "CAR"));
        regions.add(new Region("15","Bangsamoro Autonomous Region in Muslim Mindanao", "ARMM"));
        regions.add(new Region("16","Caraga Region", "CARAGA"));
        return regions;
    }
}

package app.kevin.dev.donorverifier.models;

import java.util.ArrayList;

public class Region {
    private String regcode;
    private String regname;

    public Region(String regcode, String regname) {
        this.regcode = regcode;
        this.regname = regname;
    }

    public String getRegcode() {
        return regcode;
    }

    public String getRegname() {
        return regname;
    }

    public static ArrayList<Region> getRegions(){
        ArrayList<Region> regions = new ArrayList<>();
        regions.add(new Region("01","Region 1"));
        regions.add(new Region("02","Region 2"));
        regions.add(new Region("03","Region 3"));
        regions.add(new Region("04","Region 4A"));
        regions.add(new Region("05","Region 5"));
        regions.add(new Region("06","Region 6"));
        regions.add(new Region("07","Region 7"));
        regions.add(new Region("08","Region 8"));
        regions.add(new Region("09","Region 9"));
        regions.add(new Region("10","Region 10"));
        regions.add(new Region("11","Region 11"));
        regions.add(new Region("12","Region 12"));
        regions.add(new Region("13","NCR"));
        regions.add(new Region("14","CAR"));
        regions.add(new Region("15","ARMM"));
        regions.add(new Region("16","CARAGA"));
        regions.add(new Region("17","Region 4B"));
        return regions;
    }
}

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
        regions.add(new Region("01","Region 1 Ilocos Region"));
        regions.add(new Region("02","Region 2 Cagayan Valley"));
        regions.add(new Region("03","Region 3 Central Luzon"));
        regions.add(new Region("04","Region 4A Southern Tagalog Mainland"));
        regions.add(new Region("17","Region 4B MIMAROPA"));
        regions.add(new Region("05","Region 5 Bicol Region"));
        regions.add(new Region("06","Region 6 Western Visayas"));
        regions.add(new Region("07","Region 7 Central Visayas"));
        regions.add(new Region("08","Region 8 Eastern Visayas"));
        regions.add(new Region("09","Region 9 Zamboanga Peninsula"));
        regions.add(new Region("10","Region 10 Northern Mindanao"));
        regions.add(new Region("11","Region 11 Davao Region"));
        regions.add(new Region("12","Region 12 SOCCSKSARGEN"));
        regions.add(new Region("13","National Capital Region"));
        regions.add(new Region("14","Cordillera Administrative Region"));
        regions.add(new Region("15","Bangsamoro Autonomous Region in Muslim Mindanao"));
        regions.add(new Region("16","Caraga Region"));
        return regions;
    }
}

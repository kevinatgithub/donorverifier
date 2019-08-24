package app.kevin.dev.donorverifier.models;

import java.util.ArrayList;

public class DownloadState {

    private ArrayList<RegionState> regions = new ArrayList<>();

    public ArrayList<RegionState> getRegions() {
        return regions;
    }

    public void setRegions(ArrayList<RegionState> regions) {
        this.regions = regions;
    }

    public static class RegionState{
        private String region = "";
        private String last = "0";

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getLast() {
            return last;
        }

        public void setLast(String last) {
            this.last = last;
        }
    }
}

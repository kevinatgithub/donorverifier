package app.kevin.dev.donorverifier.models;

public class StatisticsState {
    private int unsynced = 0;
    private int donors = 0;
    private int photos = 0;
    private int barangays = 0;
    private int cities = 0;
    private int provinces = 0;
    private int regions = 0;

    public int getUnsynced() {
        return unsynced;
    }

    public void setUnsynced(int unsynced) {
        this.unsynced = unsynced;
    }

    public int getDonors() {
        return donors;
    }

    public void setDonors(int donors) {
        this.donors = donors;
    }

    public int getPhotos() {
        return photos;
    }

    public void setPhotos(int photos) {
        this.photos = photos;
    }

    public int getBarangays() {
        return barangays;
    }

    public void setBarangays(int barangays) {
        this.barangays = barangays;
    }

    public int getCities() {
        return cities;
    }

    public void setCities(int cities) {
        this.cities = cities;
    }

    public int getProvinces() {
        return provinces;
    }

    public void setProvinces(int provinces) {
        this.provinces = provinces;
    }

    public int getRegions() {
        return regions;
    }

    public void setRegions(int regions) {
        this.regions = regions;
    }
}

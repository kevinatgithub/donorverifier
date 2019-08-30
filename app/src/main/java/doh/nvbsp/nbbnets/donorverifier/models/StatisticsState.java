package doh.nvbsp.nbbnets.donorverifier.models;

public class StatisticsState {
    private int donors = 0;
    private int photos = 0;

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
}

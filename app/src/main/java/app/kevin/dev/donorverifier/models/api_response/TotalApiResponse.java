package app.kevin.dev.donorverifier.models.api_response;

public class TotalApiResponse {
    private int count;
    private int withPhotos;
    private int deferred;

    public TotalApiResponse(int count, int withPhotos, int deferred) {
        this.count = count;
        this.withPhotos = withPhotos;
        this.deferred = deferred;
    }

    public int getCount() {
        return count;
    }

    public int getWithPhotos() {
        return withPhotos;
    }

    public int getDeferred() {
        return deferred;
    }
}

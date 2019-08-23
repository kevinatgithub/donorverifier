package app.kevin.dev.donorverifier.models.api_response;

public class UpdateCountResponse {
    private String status;
    private DataResponse data;

    public UpdateCountResponse(String status, DataResponse data) {
        this.status = status;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public DataResponse getData() {
        return data;
    }

    class DataResponse{
        private int count;

        public DataResponse(int count) {
            this.count = count;
        }

        public int getCount() {
            return count;
        }
    }
}

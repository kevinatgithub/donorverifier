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

    public class DataResponse{
        private int count;
        private String regcode;
        private int withPhoto;

        public int getCount() {
            return count;
        }

        public String getRegcode() {
            return regcode;
        }

        public int getWithPhoto() {
            return withPhoto;
        }
    }
}

package com.example.demo.common.response;

public class ApiResponse {
    private  String status;
    private String response;
    private String requestId;
    private int statusCode;

    public ApiResponse() {}

    public ApiResponse(String status,String response, String requestId, int statusCode) {
        this.status = status;
        this.response = response;
        this.requestId = requestId;
        this.statusCode = statusCode;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    // Simple builder
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String status;
        private String response;
        private String requestId;
        private int statusCode;


        public Builder status(String status) {
            this.status = status;
            return this;
        }

        public Builder response(String response) {
            this.response = response;
            return this;
        }

        public Builder requestId(String requestId) {
            this.requestId = requestId;
            return this;
        }

        public Builder statusCode(int statusCode) {
            this.statusCode = statusCode;
            return this;
        }

        public ApiResponse build() {
            return new ApiResponse(status,response, requestId, statusCode);
        }
    }
}

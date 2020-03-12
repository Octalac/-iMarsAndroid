package com.os.imars.operator.dao.vessel;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class VesselListResponse implements Serializable {

    @SerializedName("response")
    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public static class Response {

        @SerializedName("status")
        private int status;

        @SerializedName("message")
        private String message;

        @SerializedName("data")
        private List<VesselData> data;

        public void setStatus(int status) {
            this.status = status;
        }

        public int getStatus() {
            return status;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setData(List<VesselData> data) {
            this.data = data;
        }

        public List<VesselData> getData() {
            return data;
        }

    }

}
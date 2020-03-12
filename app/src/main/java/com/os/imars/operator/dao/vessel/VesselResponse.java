package com.os.imars.operator.dao.vessel;

import java.io.Serializable;

public class VesselResponse implements Serializable {

    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public static class Response {
        private int status;
        private String message;
        private VesselData data;

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

        public void setData(VesselData data) {
            this.data = data;
        }

        public VesselData getData() {
            return data;
        }
        

        }

    }

    
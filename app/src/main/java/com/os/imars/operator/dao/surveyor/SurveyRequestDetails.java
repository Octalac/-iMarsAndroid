package com.os.imars.operator.dao.surveyor;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SurveyRequestDetails implements Serializable {

    @SerializedName("response")
    private Response response;

    public void setResponse(Response response) {
        this.response = response;
    }

    public Response getResponse() {
        return response;
    }

    public static class Response implements Serializable {

        @SerializedName("status")
        private int status;

        @SerializedName("message")
        private String message;


        @SerializedName("data")
        private List<DataItem> data;


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

        public void setData(List<DataItem> data) {
            this.data = data;
        }

        public List<DataItem> getData() {
            return data;
        }


        public static class DataItem implements Serializable {

            @SerializedName("id")
            private String id;

            public String getReportURL() {
                return reportURL;
            }

            public void setReportURL(String reportURL) {
                this.reportURL = reportURL;
            }

            @SerializedName("survey_number")
            private String surveyNumber;

            @SerializedName("report_url")
            private String reportURL;


            public String getSurveyor_rating() {
                return surveyor_rating;
            }

            public void setSurveyor_rating(String surveyor_rating) {
                this.surveyor_rating = surveyor_rating;
            }

            @SerializedName("surveyor_rating")
            private String surveyor_rating;

            public String getBid_status() {
                return bid_status;
            }

            public void setBid_status(String bid_status) {
                this.bid_status = bid_status;
            }

            @SerializedName("bid_status")
            private String bid_status;

            public String getSurvey_category_type() {
                return survey_category_type;
            }

            public void setSurvey_category_type(String survey_category_type) {
                this.survey_category_type = survey_category_type;
            }

            @SerializedName("survey_category_type")
            private String survey_category_type;



            public String getSurveyor_id() {
                return surveyor_id;
            }

            public void setSurveyor_id(String surveyor_id) {
                this.surveyor_id = surveyor_id;
            }

            public String getOperator_name() {
                return operator_name;
            }

            public void setOperator_name(String operator_name) {
                this.operator_name = operator_name;
            }

            public String getOperator_id() {
                return operator_id;
            }

            public void setOperator_id(String operator_id) {
                this.operator_id = operator_id;
            }

            @SerializedName("port")
            private String port;

            @SerializedName("surveyor_id")
            private String surveyor_id;

            @SerializedName("operator_id")
            private String operator_id;

            @SerializedName("surveytype_name")
            private String surveycateName;

            @SerializedName("pricing")
            private String pricing;

            @SerializedName("username")
            private String username;

            @SerializedName("surveyors_name")
            private String surveyorsName;

            @SerializedName("instruction")
            private String instruction;

            @SerializedName("file_data")
            private String fileData;

            @SerializedName("start_date")
            private String startDate;

            @SerializedName("end_date")
            private String endDate;

            @SerializedName("status")
            private String status;

            public String getTransportation_cost() {
                return transportation_cost;
            }

            public void setTransportation_cost(String transportation_cost) {
                this.transportation_cost = transportation_cost;
            }

            @SerializedName("transportation_cost")
            private String transportation_cost;

            public String getSurveyor_company() {
                return surveyor_company;
            }

            public void setSurveyor_company(String surveyor_company) {
                this.surveyor_company = surveyor_company;
            }

            @SerializedName("vesselsname")
            private String vesselsname;

            @SerializedName("surveyor_company")
            private String surveyor_company;


            public String getBid_accept_status() {
                return bid_accept_status;
            }

            public void setBid_accept_status(String bid_accept_status) {
                this.bid_accept_status = bid_accept_status;
            }

            @SerializedName("vesselsemail")
            private String vesselsemail;


            @SerializedName("bid_accept_status")
            private String bid_accept_status;

            @SerializedName("vesselsaddress")
            private String vesselsaddress;

            @SerializedName("vesselscompany")
            private String vesselscompany;

            @SerializedName("agent_name")
            private String agentName;

            @SerializedName("agentsemail")
            private String agentsemail;

            @SerializedName("agentsmobile")
            private String agentsmobile;

            @SerializedName("last_status")
            private int lastStatus;

            @SerializedName("created_at")
            private String createdAt;

            @SerializedName("operator_name")
            private String operator_name;

            public String getOperator_company() {
                return operator_company;
            }

            public void setOperator_company(String operator_company) {
                this.operator_company = operator_company;
            }

            public String getOperator_company_website() {
                return operator_company_website;
            }

            public void setOperator_company_website(String operator_company_website) {
                this.operator_company_website = operator_company_website;
            }

            public String getOperator_survey_count() {
                return operator_survey_count;
            }

            public void setOperator_survey_count(String operator_survey_count) {
                this.operator_survey_count = operator_survey_count;
            }

            public String getOperator_country_name() {
                return operator_country_name;
            }

            public void setOperator_country_name(String operator_country_name) {
                this.operator_country_name = operator_country_name;
            }

            public String getOperator_average_invoice_payment_time() {
                return operator_average_invoice_payment_time;
            }

            public void setOperator_average_invoice_payment_time(String operator_average_invoice_payment_time) {
                this.operator_average_invoice_payment_time = operator_average_invoice_payment_time;
            }

            @SerializedName("operator_company")
            private String operator_company;


            @SerializedName("operator_company_website")
            private String operator_company_website;

            @SerializedName("operator_survey_count")
            private String operator_survey_count;

            @SerializedName("operator_country_name")
            private String operator_country_name;


            @SerializedName("operator_average_invoice_payment_time")
            private String operator_average_invoice_payment_time;



            public String getInvoice_url() {
                return invoice_url;
            }

            public void setInvoice_url(String invoice_url) {
                this.invoice_url = invoice_url;
            }

            @SerializedName("invoice_url")
            private String invoice_url;



            public String getImo_number() {
                return imo_number;
            }

            public void setImo_number(String imo_number) {
                this.imo_number = imo_number;
            }

            @SerializedName("imo_number")
            private String imo_number;

            public void setId(String id) {
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setSurveyNumber(String surveyNumber) {
                this.surveyNumber = surveyNumber;
            }

            public String getSurveyNumber() {
                return surveyNumber;
            }

            public void setPort(String port) {
                this.port = port;
            }

            public String getPort() {
                return port;
            }

            public void setSurveycateName(String surveycateName) {
                this.surveycateName = surveycateName;
            }

            public String getSurveycateName() {
                return surveycateName;
            }

            public void setPricing(String pricing) {
                this.pricing = pricing;
            }

            public String getPricing() {
                return pricing;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getUsername() {
                return username;
            }

            public void setSurveyorsName(String surveyorsName) {
                this.surveyorsName = surveyorsName;
            }

            public String getSurveyorsName() {
                return surveyorsName;
            }

            public void setInstruction(String instruction) {
                this.instruction = instruction;
            }

            public String getInstruction() {
                return instruction;
            }

            public void setFileData(String fileData) {
                this.fileData = fileData;
            }

            public String getFileData() {
                return fileData;
            }

            public void setStartDate(String startDate) {
                this.startDate = startDate;
            }

            public String getStartDate() {
                return startDate;
            }

            public void setEndDate(String endDate) {
                this.endDate = endDate;
            }

            public String getEndDate() {
                return endDate;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getStatus() {
                return status;
            }

            public void setVesselsname(String vesselsname) {
                this.vesselsname = vesselsname;
            }

            public String getVesselsname() {
                return vesselsname;
            }

            public void setVesselsemail(String vesselsemail) {
                this.vesselsemail = vesselsemail;
            }

            public String getVesselsemail() {
                return vesselsemail;
            }

            public void setVesselsaddress(String vesselsaddress) {
                this.vesselsaddress = vesselsaddress;
            }

            public String getVesselsaddress() {
                return vesselsaddress;
            }

            public void setVesselscompany(String vesselscompany) {
                this.vesselscompany = vesselscompany;
            }

            public String getVesselscompany() {
                return vesselscompany;
            }

            public void setAgentName(String agentName) {
                this.agentName = agentName;
            }

            public String getAgentName() {
                return agentName;
            }

            public void setAgentsemail(String agentsemail) {
                this.agentsemail = agentsemail;
            }

            public String getAgentsemail() {
                return agentsemail;
            }

            public void setAgentsmobile(String agentsmobile) {
                this.agentsmobile = agentsmobile;
            }

            public String getAgentsmobile() {
                return agentsmobile;
            }

            public void setLastStatus(int lastStatus) {
                this.lastStatus = lastStatus;
            }

            public int getLastStatus() {
                return lastStatus;
            }

            public void setCreatedAt(String createdAt) {
                this.createdAt = createdAt;
            }

            public String getCreatedAt() {
                return createdAt;
            }

            @Override
            public String toString() {
                return
                        "SurveyUserDataItem{" +
                                "id = '" + id + '\'' +
                                ",survey_number = '" + surveyNumber + '\'' +
                                ",port = '" + port + '\'' +
                                ",surveycate_name = '" + surveycateName + '\'' +
                                ",pricing = '" + pricing + '\'' +
                                ",username = '" + username + '\'' +
                                ",surveyors_name = '" + surveyorsName + '\'' +
                                ",instruction = '" + instruction + '\'' +
                                ",file_data = '" + fileData + '\'' +
                                ",start_date = '" + startDate + '\'' +
                                ",end_date = '" + endDate + '\'' +
                                ",status = '" + status + '\'' +
                                ",vesselsname = '" + vesselsname + '\'' +
                                ",vesselsemail = '" + vesselsemail + '\'' +
                                ",vesselsaddress = '" + vesselsaddress + '\'' +
                                ",vesselscompany = '" + vesselscompany + '\'' +
                                ",agent_name = '" + agentName + '\'' +
                                ",agentsemail = '" + agentsemail + '\'' +
                                ",agentsmobile = '" + agentsmobile + '\'' +
                                ",last_status = '" + lastStatus + '\'' +
                                ",created_at = '" + createdAt + '\'' +
                                "}";
            }
        }
    }
}
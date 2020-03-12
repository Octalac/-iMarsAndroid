package com.os.imars.utility;

import com.os.imars.dao.comman.CommonResponse;
import com.os.imars.dao.finance.FinanceResponse;
import com.os.imars.dao.mySurvey.SurveyListResponse;
import com.os.imars.dao.mySurvey.SurveyResponse;
import com.os.imars.dao.mySurvey.SurveyUsersListResponse;
import com.os.imars.dao.mySurveyors.MySurveysResponse;
import com.os.imars.dao.reportIssue.ReportSurveyListResponse;
import com.os.imars.operator.dao.CountryDao;
import com.os.imars.operator.dao.SurveyRequestResponse;
import com.os.imars.operator.dao.agent.AgentListResponse;
import com.os.imars.operator.dao.agent.AgentResponse;
import com.os.imars.operator.dao.appoint.SurveyTypeResponse;
import com.os.imars.operator.dao.notification.NotificationResponse;
import com.os.imars.operator.dao.surveyor.BiddingUserResponse;
import com.os.imars.operator.dao.surveyor.SurveyRequestDetails;
import com.os.imars.operator.dao.surveyor.SurveyorUseListResponse;
import com.os.imars.operator.dao.userInfo.UserListResponse;
import com.os.imars.operator.dao.userInfo.UserResponse;
import com.os.imars.operator.dao.vessel.VesselListResponse;
import com.os.imars.operator.dao.vessel.VesselResponse;
import com.os.imars.surveyor.dao.LoadAvailabilityResponse;
import com.os.imars.surveyor.dao.PortResponse;
import com.os.imars.surveyor.dao.ServiceResponse;
import com.os.imars.surveyor.dao.UserAvailableResponse;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by nitesh on 7/18/2017.
 */

public interface RxService {


    @Headers("Content-Type: application/json")
    @POST("auth/login")
    Call<UserResponse> login(@Body Map<String, Object> map);


    @Multipart
    @POST("operator/add-vessels")
    Call<VesselResponse> add_Vessel(@Part("user_id") RequestBody user_id,
                                    @Part("name") RequestBody name,
                                    @Part("imo_number") RequestBody imo_number,
                                    @Part("company") RequestBody company,
                                    @Part("same_as_company") RequestBody same_as_company,
                                    @Part("address") RequestBody address,
                                    @Part("same_as_company_address") RequestBody same_as_company_address,
                                    @Part("email") RequestBody email,
                                    @Part("additional_email") RequestBody additional_email,
                                    @Part("city") RequestBody city,
                                    @Part("state") RequestBody state,
                                    @Part("pincode") RequestBody pincode,
                                    @Part MultipartBody.Part image);


    @Headers("Content-Type: application/json")
    @POST("operator/vessels-list")
    Call<VesselListResponse> vessels_list(@Body Map<String, Object> map);


    @Multipart
    @POST("operator/edit-vessels")
    Call<VesselResponse> edit_Vessel(@Part("user_id") RequestBody user_id,
                                     @Part("name") RequestBody name,
                                     @Part("imo_number") RequestBody imo_number,
                                     @Part("company") RequestBody company,
                                     @Part("same_as_company") RequestBody same_as_company,
                                     @Part("address") RequestBody address,
                                     @Part("same_as_company_address") RequestBody same_as_company_address,
                                     @Part("email") RequestBody email,
                                     @Part("additional_email") RequestBody additionalEmail,
                                     @Part("id") RequestBody id,
                                     @Part("city") RequestBody city,
                                     @Part("state") RequestBody state,
                                     @Part("pincode") RequestBody pincode,
                                     @Part MultipartBody.Part image);


    @Multipart
    @POST("operator/add-agents")
    Call<AgentResponse> add_agent(@Part("user_id") RequestBody user_id,
                                  @Part("first_name") RequestBody first_name,
                                  @Part("email") RequestBody email,
                                  @Part("mobile") RequestBody mobile,
                                  @Part MultipartBody.Part image);


    @Headers("Content-Type: application/json")
    @POST("operator/agents-list")
    Call<AgentListResponse> agents_list(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/change-device-id")
    Call<CommonResponse> change_device_id(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/operator-custom-survey-accept")
    Call<SurveyResponse> operator_custom_bidding_accept(@Body Map<String, Object> map);


    @Multipart
    @POST("operator/edit-agents")
    Call<AgentResponse> edit_Agent(@Part("user_id") RequestBody user_id,
                                   @Part("first_name") RequestBody first_name,
                                   @Part("email") RequestBody email,
                                   @Part("mobile") RequestBody mobile,
                                   @Part("id") RequestBody id,
                                   @Part MultipartBody.Part image);


    @Multipart
    @POST("operator/report-issue-submit")
    Call<CommonResponse> report_issue_submit(@Part("user_id") RequestBody user_id,
                                             @Part("survey_id") RequestBody survey_id,
                                             @Part("comment") RequestBody comment,
                                             @Part MultipartBody.Part file);


    @Multipart
    @POST("operator/add-operators")
    Call<UserResponse> add_operator(@Part("user_id") RequestBody user_id,
                                    @Part("email") RequestBody email);


    @Headers("Content-Type: application/json")
    @POST("operator/operators-list")
    Call<UserListResponse> operator_list(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/resend-operators")
    Call<UserResponse> resend_operators(@Body Map<String, Object> map);



    @Multipart
    @POST("operator/edit-operators")
    Call<UserResponse> edit_operator(@Part("user_id") RequestBody user_id,
                                     @Part("edit_id") RequestBody edit_id,
                                     @Part("email") RequestBody email);

    @Headers("Content-Type: application/json")
    @POST("operator/my-profile")
    Call<UserResponse> my_profile(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/delete-operators")
    Call<UserListResponse> delete_operators(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/deleted-surveyor")
    Call<UserListResponse> deleted_surveyor(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/survey-category")
    Call<SurveyTypeResponse> survey_category(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/survey-user-list")
    Call<SurveyorUseListResponse> survey_user_list(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/report-survey-list")
    Call<ReportSurveyListResponse> report_survey_list(@Body Map<String, Object> map);


    @Multipart
    @POST("operator/request-survey")
    Call<SurveyRequestResponse> request_survey(@Part("user_id") RequestBody user_id,
                                               @Part("port_id") RequestBody port_data,
                                               @Part("ship_id") RequestBody ship_id,
                                               @Part("start_date") RequestBody start_date,
                                               @Part("end_date") RequestBody end_date,
                                               @Part("survey_type_id") RequestBody survey_cat_id,
                                               @Part("surveyors_id") RequestBody surveyors_id,
                                               @Part("agent_id") RequestBody agent_id,
                                               @Part("instruction") RequestBody instructionData,
                                               @Part("status") RequestBody status,
                                               @Part MultipartBody.Part image);


    @Multipart
    @POST("operator/custom-request-survey")
    Call<SurveyRequestResponse> custom_request_survey(@Part("user_id") RequestBody user_id,
                                                      @Part("port_id") RequestBody port_data,
                                                      @Part("ship_id") RequestBody ship_id,
                                                      @Part("start_date") RequestBody start_date,
                                                      @Part("end_date") RequestBody end_date,
                                                      @Part("survey_type_id") RequestBody survey_cat_id,
                                                      @Part("surveyors_id") RequestBody surveyors_id,
                                                      @Part("agent_id") RequestBody agent_id,
                                                      @Part("instruction") RequestBody instructionData,
                                                      @Part("status") RequestBody status,
                                                      @Part MultipartBody.Part image);


    @Headers("Content-Type: application/json")
    @POST("operator/survey-list")
    Call<SurveyListResponse> survey_list(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/survey-all")
    Call<MySurveysResponse> survey_all(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/survey-details")
    Call<SurveyRequestDetails> survey_details(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/change-start-date")
    Call<CommonResponse> change_start_date(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/assign-to-surveyor")
    Call<CommonResponse> assign_to_surveyor(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/assign-to-operator")
    Call<CommonResponse> assign_to_operator(@Body Map<String, Object> map);


    @Multipart
    @POST("operator/edit_profile")
    Call<UserResponse> edit_profile(@Part("user_id") RequestBody user_id,
                                    @Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("email") RequestBody email,
                                    @Part("type") RequestBody designation,
                                    @Part("mobile") RequestBody mobile,
                                    @Part("address") RequestBody address,
                                    @Part("company") RequestBody company_name,
                                    @Part("company_website") RequestBody company_website,
                                    @Part("country_id") RequestBody country,
                                    @Part MultipartBody.Part image);


    @Headers("Content-Type: application/json")
    @POST("operator/notification_list")
    Call<NotificationResponse> notification_list(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("auth/user-setting")
    Call<UserAvailableResponse> user_setting(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/user-survey-type")
    Call<ServiceResponse> surveyor_services(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/event-load")
    Call<LoadAvailabilityResponse> event_load(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/user-calender-avail")
    Call<LoadAvailabilityResponse> change_availability(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/custom-survey-user-list")
    Call<BiddingUserResponse> custom_survey_biding_user(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/user-survey-port")
    Call<PortResponse> surveyor_port(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/notification_delete")
    Call<CommonResponse> notification_delete(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/notification_read")
    Call<CommonResponse> notification_read(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/addshipfavourite")
    Call<VesselResponse> add_ship_favourite(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/survey-accept-reject")
    Call<SurveyResponse> survey_accept_reject(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/custom-survey-accept-reject")
    Call<SurveyResponse> custom_survey_accept_reject(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/cancel-survey")
    Call<SurveyResponse> cancel_survey(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/country-list")
    Call<CountryDao> country_list();

    @Headers("Content-Type: application/json")
    @POST("auth/forgotpassword")
    Call<CommonResponse> forgot_password(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/edit-agent-insurvey")
    Call<CommonResponse> edit_agent_survey(@Body Map<String, Object> map);


    @Multipart
    @POST("operator/report-submit")
    Call<SurveyResponse> report_submit(@Part("survey_id") RequestBody survey_id,
                                       @Part("surveyor_id") RequestBody surveyor_id,
                                       @Part("no_of_days") RequestBody no_of_days,
                                       @Part MultipartBody.Part file);

    @Headers("Content-Type: application/json")
    @POST("operator/report-accept")
    Call<SurveyResponse> report_accept(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/survey-list-past")
    Call<SurveyListResponse> survey_list_past(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/addrating")
    Call<CommonResponse> addRating(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/survey-filter-user")
    Call<SurveyUsersListResponse> survey_user(@Body Map<String, Object> map);

    @Headers("Content-Type: application/json")
    @POST("operator/assign-surveyor-list")
    Call<SurveyUsersListResponse> assign_surveyor_list(@Body Map<String, Object> map);



    @Headers("Content-Type: application/json")
    @POST("operator/finance")
    Call<FinanceResponse> finance(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/logout")
    Call<CommonResponse> logout(@Body Map<String, Object> map);


    @Headers("Content-Type: application/json")
    @POST("operator/chat-email")
    Call<CommonResponse> chat_email(@Body Map<String, Object> map);

}


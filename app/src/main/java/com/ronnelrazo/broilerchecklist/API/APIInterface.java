package com.ronnelrazo.broilerchecklist.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {


    @FormUrlEncoded
    @POST("Login_Broiler_Checklist")
    Call<API_Response> LOGIN(
            @Field("username") String username,
            @Field("password") String password
    );


    @FormUrlEncoded
    @POST("Broiler_Checklist")
    Call<API_Response_checklist> Broiler_Checklist(
            @Field("org_wip") String org_wip
    );


    @FormUrlEncoded
    @POST("Farm_Broiler_Checklist")
    Call<API_Response_Farm> Farm_Broiler_Checklist(
            @Field("org_wip") String org_wip
    );


    @FormUrlEncoded
    @POST("Address_Broiler_Checklist")
    Call<API_Response_Address> Address_Broiler_Checklist(
            @Field("org_wip") String org_wip
    );

    @FormUrlEncoded
    @POST("FHF_Broiler_Checklist")
    Call<API_Response_FHF> FHF_Broiler_Checklist(
            @Field("org_wip") String org_wip,
            @Field("farm_code") String farm_code
    );


    @FormUrlEncoded
    @POST("REASON_Broiler_Checklist")
    Call<API_Response_reason> REASON_Broiler_Checklist(
            @Field("org_wip") String org_wip
    );


    @FormUrlEncoded
    @POST("HEADER_Broiler_Checklist")
    Call<API_Response_header> HEADER_Broiler_Checklist(
            @Field("org_wip") String org_wip,
            @Field("farm_code") String farm_code
    );

    @FormUrlEncoded
    @POST("INSERT_HEADER_Broiler_Checklist")
    Call<API_TRN_HEADER> INSERT_HEADER_Broiler_Checklist(
            @Field("username") String username,
            @Field("auditDate") String auditDate,
            @Field("farmOrg") String farmOrg,
            @Field("farmName") String farmName,
            @Field("stockBalance") String stockBalance,
            @Field("dateIn") String dateIn,
            @Field("age") String age,
            @Field("houseFlock") String houseFlock,
            @Field("broilerCount") String broilerCount,
            @Field("broilerWeight") String broilerWeight,
            @Field("broilerRejectCount") String broilerRejectCount,
            @Field("broilerRejectWeight") String broilerRejectWeight,
            @Field("broilerAutoCountFlag") String broilerAutoCountFlag,
            @Field("cancelFlag") String cancelFlag,
            @Field("confirmFlag") String confirmFlag,
            @Field("createDate") String createDate
    );

    @FormUrlEncoded
    @POST("INSERT_DETAILS_Broiler_Checklist")
    Call<API_TRN_DETAILS> INSERT_DETAILS_Broiler_Checklist(
            @Field("documentNo") String documentNo,
            @Field("auditDate") String auditDate,
            @Field("ext") String ext,
            @Field("reasonId") String reasonId,
            @Field("reasonName") String reasonName,
            @Field("birdCount") String birdCount,
            @Field("org_wip") String org_wip,
            @Field("farm_org") String farm_org,
            @Field("houseFlock") String houseFlock
    );


    @FormUrlEncoded
    @POST("INSERT_signature_Broiler_Checklist")
    Call<API_SIGNATURE> INSERT_signature_Broiler_Checklist(
            @Field("documentNo") String documentNo,
            @Field("auditDate") String auditDate,
            @Field("farm_org") String farm_org,
            @Field("house_flock") String house_flock,
            @Field("preparedby") String preparedby,
            @Field("preparedsign") String preparedsign,
            @Field("salesman") String salesman,
            @Field("salesmansign") String salesmansign,
            @Field("farm_manager") String farm_manager,
            @Field("farm_managersign") String farm_managersign
    );

    @FormUrlEncoded
    @POST("TRANSACTION_Broiler_Checklist")
    Call<API_TRANSACTION> TRANSACTION_Broiler_Checklist(
            @Field("org_code") String org_code,
            @Field("user_create") String user_create,
            @Field("audit_date") String audit_date,
            @Field("flock") String flock
    );





}

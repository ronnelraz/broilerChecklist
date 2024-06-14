package com.ronnelrazo.broilerchecklist.API;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface_CBH {


    @FormUrlEncoded
    @POST("pdf")
    Call<API_PDF> pdf(
            @Field("doc_no") String doc_no
    );



}

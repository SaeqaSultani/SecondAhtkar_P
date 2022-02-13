package com.example.women.networking_senddata;


import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;

public interface ApiInterfaceSendReport {

    @Multipart
    @POST("complaints/{person_id}")
    Call<ResponseBody> uploadDataHoard(
            @Part("c_address") RequestBody c_address,
            @Part("c_province") RequestBody c_province,
            @Part("c_district") RequestBody c_district,
            @Part("c_ehtakar_position_name") RequestBody c_ehtakar_position_name,
            @Part("c_cost") RequestBody c_cost,
            @Part("c_material_name") RequestBody c_material_name,
            @Part MultipartBody.Part c_photo,
            @Path ("person_id") int person_id
    );


    @Multipart
    @POST("places/{person_id}")
    Call<ResponseBody> uploadDataPlaces(
            @Part("c_province") RequestBody c_province,
            @Part("c_district") RequestBody c_district,
            @Part("c_ehtakar_position_name") RequestBody c_ehtakar_position_name,
            @Part("c_address") RequestBody c_address,
            @Part MultipartBody.Part c_photo,
            @Path ("person_id") int person_id
    );


    @Multipart
    @POST("register_person")
    Call<ResponseBody> signupMethod(
            @Part("r_name") RequestBody r_name,
            @Part("r_last_name") RequestBody r_last_name,
            @Part("r_province") RequestBody r_province,
            @Part("r_district") RequestBody r_district,
            @Part("r_location") RequestBody r_location,
            @Part("r_phone") RequestBody r_phone,
            @Part("r_password") RequestBody r_password

            );



    @GET("login/{r_phone}/{r_password}")
    Call<ClassSignin> login(@Path("r_phone") String phone_number,
                             @Path("r_password") String password);
}

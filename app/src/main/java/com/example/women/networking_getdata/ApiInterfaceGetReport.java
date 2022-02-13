package com.example.women.networking_getdata;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterfaceGetReport {


    @GET("complaints")//getInformation
    Call<List<ClassGetReport>> getDataHoard();

    @GET("places")//getInformation
    Call<List<ClassGetReport>> getDataPlaces();
}

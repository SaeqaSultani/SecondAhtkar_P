package com.example.women.networking_senddata;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class ClassSendReport {

    @SerializedName("c_province")
    private String Province;

    @SerializedName("c_district")
    private String District;

    @SerializedName("c_ehtakar_position_name")
    private String EhtakarPositionName;

    @SerializedName("c_address")
    private String Address;

    @SerializedName("c_photo")
    private String Image;

    @SerializedName("response")
    private String Response;

    public String getResponse() {
        return Response;
    }
}

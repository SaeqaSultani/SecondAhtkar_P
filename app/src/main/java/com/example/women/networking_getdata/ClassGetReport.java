package com.example.women.networking_getdata;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

public class ClassGetReport {


    @SerializedName("c_province")
    private String Province;

    @SerializedName("c_district")
    private String District;

    @SerializedName("c_ehtakar_position_name")
    private String EhtakarPositionName;


    @SerializedName("c_material_name")
    private String Name;

    @SerializedName("c_cost")
    private String Cost;

    @SerializedName("c_address")
    private String Address;

    @SerializedName("c_answer")
    private String Answer;

    @SerializedName("c_photo")
    private String Image;

    public String getName() {
        return Name;
    }

    public String getCost() {
        return Cost;
    }

    public String getAddress() { return Address; }

    public String getAnswer() {
        return Answer;
    }

    public String getImage() {
        return Image;
    }

    public String getProvince() {
        return Province;
    }

    public String getDistrict() {
        return District;
    }

    public String getEhtakarPositionName() {
        return EhtakarPositionName;
    }
}

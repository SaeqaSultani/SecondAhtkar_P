package com.example.women.networking_senddata;

import com.google.gson.annotations.SerializedName;

public class ClassSignin {

    @SerializedName("r_id")
    private Integer Id ;

    @SerializedName("r_name")
    private String Name;

    @SerializedName("r_last_name")
    private String Last_Name ;

    @SerializedName("r_phone")
    private String Phone;

    @SerializedName("r_password")
    private String Password;

    @SerializedName("r_province")
    private String Province ;

    @SerializedName("r_district")
    private String District ;

    @SerializedName("r_location")
    private String Location ;

    @SerializedName("response")
    private String Response ;

    //-------
    @SerializedName("SignIn")
    private String signIn;

    public String getSignIn() {
        return signIn;
    }


    public Integer getId() {
        return Id;
    }

    public String getName() {
        return Name;
    }

    public String getLast_Name() {
        return Last_Name;
    }

    public String getPhone() {
        return Phone;
    }

    public String getPassword() {
        return Password;
    }

    public String getProvince() {
        return Province;
    }

    public String getDistrict() {
        return District;
    }

    public String getLocation() {
        return Location;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }
}

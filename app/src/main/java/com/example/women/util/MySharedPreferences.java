package com.example.women.util;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {

    static MySharedPreferences mySharedPreferences;
    private SharedPreferences sharedPreferences;

    private MySharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
    }


    public static MySharedPreferences getInstance(Context context){
        if (mySharedPreferences == null){
             return  new MySharedPreferences(context);
        }
        return  mySharedPreferences;
    }


    public void setId(int id){
        sharedPreferences.edit().putInt("ID",id).apply();
    }


    public int getId(){
        return sharedPreferences.getInt("ID",-1);
    }





    //sign in stuts
    public void setSignInStatus(boolean status){
        sharedPreferences.edit().putBoolean("SIGN_IN_STATUS",status).apply();
    }

    public boolean getSignInStatus(){
        return sharedPreferences.getBoolean("SIGN_IN_STATUS", false);
    }

    //slidShow in status

    public void setSliDShowStatus(boolean status){
        sharedPreferences.edit().putBoolean("SLID_SHOW_STATUS",status).apply();
    }

    public boolean getSliDShowStatus(){
        return sharedPreferences.getBoolean("SLID_SHOW_STATUS", false);
    }

    public void clearAll(){
        sharedPreferences.edit().clear().apply();
    }

}

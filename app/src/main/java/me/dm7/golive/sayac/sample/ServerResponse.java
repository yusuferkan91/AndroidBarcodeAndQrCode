package me.dm7.golive.sayac.sample;

import com.google.gson.annotations.SerializedName;

class ServerResponse {
    // variable name should be same as in the json response from php
    @SerializedName("sayac")
    String sayac;

    @SerializedName("endeks")
    String endeks;
    String getMessage() {
        return endeks;
    }
    String getSuccess() {
        return sayac;
    }
}
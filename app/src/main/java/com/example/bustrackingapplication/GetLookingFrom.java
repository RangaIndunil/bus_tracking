package com.example.bustrackingapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GetLookingFrom {
    private SharedPreferences prefs;

    public GetLookingFrom(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setfrom(String from) {
        prefs.edit().putString("from", from).commit();
    }

    public String getfrom() {
        String usename = prefs.getString("from","");
        return usename;
    }
}
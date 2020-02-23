package com.example.bustrackingapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GetLookingName {
    private SharedPreferences prefs;

    public GetLookingName(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setname(String name) {
        prefs.edit().putString("name", name).commit();
    }

    public String getname() {
        String name = prefs.getString("name","");
        return name;
    }
}
package com.example.bustrackingapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GetLookingTo {
    private SharedPreferences prefs;

    public GetLookingTo(Context cntx) {
        // TODO Auto-generated constructor stub
        prefs = PreferenceManager.getDefaultSharedPreferences(cntx);
    }

    public void setto(String to) {
        prefs.edit().putString("to", to).commit();
    }

    public String getto() {
        String to = prefs.getString("to","");
        return to;
    }
}
package com.fungsitama.dhsshopee.util;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * Created by DAD on 12/4/2016.
 */

public class SessionManager {
    public void setPreferences(Context context, String str, String str2) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mypreferences", 0).edit();
        edit.putString(str, str2);
        edit.commit();
    }

    public String getPreferences(Context context, String str) {
        return context.getSharedPreferences("mypreferences", 0).getString(str, "");
    }
}

package com.rf17.nexpenses.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.rf17.nexpenses.R;

import java.util.HashSet;
import java.util.Set;

public class AppPreferences {
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public static final String KeyPassword = "prefPassword";
    public static final String KeyPasswordBoolean = "prefPasswordBoolean";
    public static final String KeySortMode = "prefSortMode";

    public static final String KeyPrimaryColor = "prefPrimaryColor";
    public static final String KeyAccentColor = "prefAccentColor";
    public static final String KeyNavigationColor = "prefNavigationColor";

    public AppPreferences(Context context) {
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.editor = sharedPreferences.edit();
        this.context = context;
    }

    public String getPassoword() {
        return sharedPreferences.getString(KeyPassword, "");
    }
    public void setPassword(String password) {
        editor.putString(KeySortMode, password);
        editor.commit();
    }


    public Boolean getPassowordBooleanPref() {
        return sharedPreferences.getBoolean(KeyPasswordBoolean, false);
    }
    public void setPassowordBooleanPref(Boolean res) {
        editor.putBoolean(KeyPasswordBoolean, res);
        editor.commit();
    }

    public int getPrimaryColorPref() {
        return sharedPreferences.getInt(KeyPrimaryColor, context.getResources().getColor(R.color.primary));
    }
    public void setPrimaryColorPref(Integer res) {
        editor.putInt(KeyPrimaryColor, res);
        editor.commit();
    }

    public int getAccentColorPref() {
        return sharedPreferences.getInt(KeyAccentColor, context.getResources().getColor(R.color.fab));
    }
    public void setAccentColorPref(Integer res) {
        editor.putInt(KeyAccentColor, res);
        editor.commit();
    }

    public Boolean getNavigationColorPref() {
        return sharedPreferences.getBoolean(KeyNavigationColor, false);
    }
    public void setNavigationColorPref(Boolean res) {
        editor.putBoolean(KeyNavigationColor, res);
        editor.commit();
    }

    /*
    public Boolean getFABShowPref() {
        return sharedPreferences.getBoolean(KeyFABShow, false);
    }
    public void setFABShowPref(Boolean res) {
        editor.putBoolean(KeyFABShow, res);
        editor.commit();
    }
    */

    public String getSortMode() {
        return sharedPreferences.getString(KeySortMode, "1");
    }
    public void setSortMode(String res) {
        editor.putString(KeySortMode, res);
        editor.commit();
    }

}

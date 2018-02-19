package com.robj.promolibrary;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Rob J on 05/11/17.
 */

class PrefUtils {

    private static final String TAG = PrefUtils.class.getSimpleName();
    private static String PREF_SUFFIX = "_PromoLib";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(getPrefName(context), Context.MODE_PRIVATE);
    }

    private static String getPrefName(Context context) {
        return context.getPackageName() + PREF_SUFFIX;
    }

    static void writeStringPref(Context context, String name, String s) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putString(name, s);
        editor.apply();
    }

    static String readStringPref(Context context, String name) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getString(name, "");
    }

    static long readLongPref(Context context, String name) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getLong(name, 0);
    }

    static void writeLongPref(Context context, String name, long l) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putLong(name, l);
        editor.apply();
    }

    static int readIntPref(Context context, String name) {
        SharedPreferences sp = getSharedPreferences(context);
        return sp.getInt(name, 0);
    }

    static void writeIntPref(Context context, String name, int l) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        editor.putInt(name, l);
        editor.apply();
    }

    static void removePrefs(Context context, String[] prefs) {
        SharedPreferences.Editor editor = getSharedPreferences(context).edit();
        for(String pref : prefs)
            editor.remove(pref);
        editor.apply();
    }

}

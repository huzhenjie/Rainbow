package com.scrat.lib.framework;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by scrat on 2017/4/27.
 */
public class BaseSharedPreferences {
    private SharedPreferences preferences;

    public BaseSharedPreferences() {
    }

    public BaseSharedPreferences(Context applicationContext, String fileName) {
        init(applicationContext, fileName);
    }

    protected void init(Context applicationContext, String fileName) {
        if (preferences == null) {
            preferences = applicationContext.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        }
    }

    private static void apply(SharedPreferences.Editor editor) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        } else {
            editor.commit();
        }
    }

    public static void syncApply(SharedPreferences.Editor editor) {
        editor.commit();
    }

    public long getLong(String key, long defaultValue) {
        return preferences.getLong(key, defaultValue);
    }

    public void setLong(String key, long value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        apply(editor);
    }

    public int getInt(String key, int defaultValue) {
        return preferences.getInt(key, defaultValue);
    }

    public void setInt(String key, int value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        apply(editor);
    }

    public float getFloat(String key, float defaultValue) {
        return preferences.getFloat(key, defaultValue);
    }

    public void setFloat(String key, float value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putFloat(key, value);
        apply(editor);
    }

    public String getString(String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void setString(String key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        apply(editor);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return preferences.getBoolean(key, defaultValue);
    }

    public void setBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        apply(editor);
    }

    public void removeItem(String key) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove(key);
        apply(editor);
    }
}

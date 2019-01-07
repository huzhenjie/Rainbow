package com.scrat.lib.uitl;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class EnvUtil {
    private EnvUtil() {
        throw new AssertionError("No instances.");
    }

    public static String getVersionName(Context ctx) {
        return getVersionName(ctx, "");
    }

    public static String getVersionName(Context ctx, String defaultVersionName) {
        if (ctx == null) {
            return defaultVersionName;
        }
        try {
            String pkName = ctx.getPackageName();
            return ctx.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return defaultVersionName;
        }
    }

    public static int getVersionCode(Context ctx) {
        return getVersionCode(ctx, 1);
    }

    public static int getVersionCode(Context ctx, int defaultVersionCode) {
        if (ctx == null) {
            return defaultVersionCode;
        }
        try {
            String pkName = ctx.getPackageName();
            return ctx.getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return defaultVersionCode;
    }

    public static String getChannelName(Context ctx) {
        return getChannelName(ctx, "");
    }

    public static String getChannelName(Context ctx, String defaultChannelName) {
        return getMetaData(ctx, "UMENG_CHANNEL", defaultChannelName);
    }

    public static String getMetaData(Context ctx, String key, String defaultValue) {
        if (ctx == null) {
            return defaultValue;
        }
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager == null) {
                return defaultValue;
            }
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(
                    ctx.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo == null) {
                return defaultValue;
            }
            if (applicationInfo.metaData != null) {
                return applicationInfo.metaData.getString(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return defaultValue;
    }
}

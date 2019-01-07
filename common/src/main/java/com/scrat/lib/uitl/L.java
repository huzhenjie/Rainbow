package com.scrat.lib.uitl;

import android.util.Log;

import com.scrat.lib.BuildConfig;

import java.util.Locale;

/**
 * Log工具
 * Created by scrat on 16/6/29.
 */
public class L {

    private static final String LOG_TAG = "err";
    private static final String LOG_FORMAT = "%1$s\n%2$s";
    private static final String STACK_FORMAT = "%s(%s:%d) %s";

    public static final boolean CAN_WRITE_LOGS = BuildConfig.DEBUG; // 总开关
    public static final boolean CAN_WRITE_VERBOSE_LOGS = true;
    public static final boolean CAN_WRITE_DEBUG_LOGS = true;
    public static final boolean CAN_WRITE_INFO_LOGS = true;
    public static final boolean CAN_WRITE_WARN_LOGS = true;
    public static final boolean CAN_WRITE_ERROR_LOGS = true;

    private L() {
        throw new AssertionError("No instances.");
    }

    private static StackTraceElement getCallerStackTraceElement() {
        return Thread.currentThread().getStackTrace()[4];
    }

    public static void v(String message, Object... args) {
        if (!CAN_WRITE_VERBOSE_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.VERBOSE, null, message, caller, args);
    }

    public static void d(String message, Object... args) {
        if (!CAN_WRITE_DEBUG_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.DEBUG, null, message, caller, args);
    }

    public static void i(String message, Object... args) {
        if (!CAN_WRITE_INFO_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.INFO, null, message, caller, args);
    }

    public static void w(String message, Object... args) {
        if (!CAN_WRITE_WARN_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.WARN, null, message, caller, args);
    }

    public static void e(Throwable ex) {
        if (!CAN_WRITE_ERROR_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.ERROR, ex, null, caller);
    }

    public static void e(String message, Object... args) {
        if (!CAN_WRITE_ERROR_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.ERROR, null, message, caller, args);
    }

    public static void e(Throwable ex, String message, Object... args) {
        if (!CAN_WRITE_ERROR_LOGS) {
            return;
        }

        StackTraceElement caller = getCallerStackTraceElement();
        log(Log.ERROR, ex, message, caller, args);
    }

    private static String getFormatMessage(
            StackTraceElement caller, String message, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }

        return String.format(Locale.getDefault(), STACK_FORMAT, caller.getMethodName(),
                caller.getFileName(), caller.getLineNumber(), message);
    }

    private static void log(
            int priority, Throwable ex, String message, StackTraceElement caller, Object... args) {
        if (!CAN_WRITE_LOGS) {
            return;
        }

        String log;
        if (ex == null) {
            log = getFormatMessage(caller, message, args);
        } else {
            String logMessage =
                    message == null ? ex.getMessage() : getFormatMessage(caller, message, args);
            String logBody = Log.getStackTraceString(ex);
            log = String.format(LOG_FORMAT, logMessage, logBody);
        }

        Log.println(priority, LOG_TAG, log);
    }

}

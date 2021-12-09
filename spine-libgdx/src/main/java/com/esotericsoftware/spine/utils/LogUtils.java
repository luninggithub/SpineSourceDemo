package com.esotericsoftware.spine.utils;

import android.util.Log;

public class LogUtils {
    private static final String TAG = "SPINE-";

    private static final boolean DEBUG = true;

    public static void i(String tag, String msg) {
        if (DEBUG) {
            Log.i(TAG + tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (DEBUG) {
            Log.e(TAG + tag, msg);
        }
    }
    public static void e(String tag, String msg, Exception e) {
        if (DEBUG) {
            Log.e(TAG + tag, msg, e);
        }
    }

    public static void w(String tag, String msg) {
        if (DEBUG) {
            Log.w(TAG + tag, msg);
        }
    }

    public static void d(String tag, String msg) {
        if (DEBUG) {
            Log.d(TAG + tag, msg);
        }
    }

}

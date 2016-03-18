package com.manoharan.narendra_kumar.wlimate.cache;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * Created by naren on 17-03-2016.
 */
public class RawCache {
    public static final String PREFIX = "";
    public static final String SEPERATOR = "";
    public static final long RETENTION_TIME = 1000 * 60 * 5; // 5 min

    public static File getRoot(Context ctx) {
        return ctx.getFilesDir();
    }

    public static String generateKey(String type) {
        return PREFIX + SEPERATOR + type;
    }

    public static void cache(Context ctx, String type, String data) {
        if (ctx != null & data != null && !"".equals(data)) {
            File file = null;
            FileWriter writer = null;

            try {
                file = new File(getRoot(ctx), generateKey(type));
                writer = new FileWriter(file);
                writer.write(data);
            } catch (Exception exc) {

            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Exception e) {

                    }
                }
            }
        }

    }

    public static String fromCache(Context ctx, String type) {
        String data = null;

        if (ctx != null) {
            File file = null;
            FileReader reader = null;
            BufferedReader buf = null;

            try {
                file = new File(getRoot(ctx), generateKey(type));
                reader = new FileReader(file);
            } catch (Exception e) {
                if (reader != null)
                    try {
                        reader.close();
                    } catch (Exception exc) {

                    }
            }
            if (buf != null) {
                try {
                    buf.close();
                } catch (Exception exc) {

                }
            }
        }
        return data;
    }

    public static void isInCache(Context ctx, String type) {
        boolean isInCache = false;

        if (ctx != null) {
            File file = new File(getRoot(ctx), generateKey(type));
        }
    }
}
























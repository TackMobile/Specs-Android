package com.tackmobile.specs.Utils;

import android.os.Build;

/**
 * Created by anato on 4/25/2016.
 */
public class DeviceUtils {

    public  static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;

        if (model.startsWith(manufacturer.toUpperCase()) || model.startsWith(manufacturer) ||
                model.startsWith(capitalize(manufacturer))) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}

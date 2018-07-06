package com.muhammadelsayed.bybike_rider.Utils;

import android.util.Log;

public class Utils {

    private static final String TAG = Utils.class.getSimpleName();

    // Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    // Fragments Tags
    public static final String LOGIN_FRAGMENT = "LOGIN_FRAGMENT";
    public static final String SING_UP_FRAGMENT = "SingUpFragment";
    public static final String FORGOT_PASSWORD_FRAGMENT = "FORGOT_PASSWORD_FRAGMENT";
    public static final String EXTENDED_SIGN_UP = "ExtendedSignUpFragment";


    /**
     * This method split any name to two names, first name, last name
     *
     * @param name is the name that will be split
     * @return Strign array holding the first name, and the last name.
     */
    public static String[] splitName(String name) {
        Log.wtf(TAG, "splitName() has been instantiated");

        String[] names = name.split(" ", 2);
        return names;

    }

    public static String splitAddress(String location) {
        Log.wtf(TAG, "splitName() has been instantiated");


        String[] address = location.split(", Mansoura", 2);
        return address[0];
    }




}

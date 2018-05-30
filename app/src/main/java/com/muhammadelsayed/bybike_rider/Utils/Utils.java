package com.muhammadelsayed.bybike_rider.Utils;

public class Utils {

    // Email Validation pattern
    public static final String regEx = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}\\b";

    // Fragments Tags
    public static final String LoginFragment = "LoginFragment";
    public static final String SignUpFragment = "SingUpFragment";
    public static final String ForgotPasswordFragment = "ForgotPasswordFragment";


    /**
     * This method split any name to two names, first name, last name
     *
     * @param name is the name that will be split
     * @return Strign array holding the first name, and the last name.
     */
    public static String[] splitName(String name) {
        String[] names = name.split(" ", 2);
        return names;
    }

}

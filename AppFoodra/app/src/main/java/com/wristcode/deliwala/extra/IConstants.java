package com.wristcode.deliwala.extra;

import android.net.Uri;

public interface IConstants
{
    public static String API_PATH ="http://www.appfoodra.com/api/app-manager/get-functionality/";
    public static String API_MEDIA ="http://192.168.1.39/otrasi_new/";
    public static Uri PHONE_URI_COMMON = Uri.parse("tel:9611247955");
    public static Uri PHONE_URI_FOOD = Uri.parse("tel:9449073509");
    public static String OTP_PATH ="http://sms.chotaweb.in/api/sendhttp.php?";
    public static String OTP_AUTH ="5361A35so7gHDff5976d024";
    public static String OTP_SENDER ="GRTREE";
    public static int OTP_ROUTE =4;
    public static final String USER_PREFS ="user_prefs";
    public static final int CONNECTION_TIMEOUT = 20000;
    public static final int READ_TIMEOUT = 20000;
    public static final String radiusKm = "3";
}

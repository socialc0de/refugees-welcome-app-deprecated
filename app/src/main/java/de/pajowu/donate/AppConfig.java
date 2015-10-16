package de.pajowu.donate;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
/**
 *
 * @author livin
 *
 */
public class AppConfig {

    public static final String PACKAGE_NAME = "de.pajowu.donate";
    public static final String RECEIVER = PACKAGE_NAME + ".RECEIVER";
    public static final String RESULT_DATA_KEY = PACKAGE_NAME
            + ".RESULT_DATA_KEY";
    public static final String LOCATION_DATA_EXTRA = PACKAGE_NAME
            + ".LOCATION_DATA_EXTRA";
    public static final String USER_LAT = "user_lat";
    public static final String USER_LNG = "user_lng";
    public static final String USER_LOCATION = "user_location";
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final int PICKUP_LOCATION = 999;


}

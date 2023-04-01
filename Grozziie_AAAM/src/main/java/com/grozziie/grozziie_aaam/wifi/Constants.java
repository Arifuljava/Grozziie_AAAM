package com.grozziie.grozziie_aaam.wifi;

public interface Constants {

    public static final String DEMO_PREFERENCES = "demo_preferences";
    public static final String CONTROLLER_WIFI_CONFIGURATION = "controller_wifi_configuration";
    public static final String CONTROLLER_PRINTER_CONFIGURATION = "controller_printer_configuration";
    public static final String CONTROLLER_PRINTER = "printer";
    public static final String CONTROLLER_WIFI = "wifi";
    public static final String CONTROLLER_MOBILE = "mobile";

    public static final int PRINTER_STATUS_COMPLETED = 1;
    public static final int PRINTER_STATUS_CANCELLED = 0;

    public static final int REQUEST_CODE_PRINTER = 1000;
    public static final int REQUEST_CODE_WIFI = 999;
    public static final int RESULT_CODE_PRINTER = 1001;
    public static final int RESULT_CODE_PRINTER_CONNECT_FAILED = 1002;

    public static final String CONTROLLER_PDF_FOLDER = "print_demo_folder";

    int WIFI_MAX_LEVEL = 100;


    long TIME_SCAN_DELAY_NORMAL = 500L;

    long TIME_SCAN_DELAY_LONG = 5000L;

    /**
     * Getting data from list
     */
    long TIME_REFRESH_WIFI_INTERVAL = 2000L;
    String KEY_WIFI_SSID = "SSID";

}

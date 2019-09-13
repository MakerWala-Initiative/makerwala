package com.uvtech.makerwala;

import android.os.Environment;

import java.io.File;

public class ApplicationConstants {

    public static final String YOUTUBE_API = "AIzaSyB7LBeorMM4Rhn_PIxSEb6tZQKMoaeG8Mw";//"AIzaSyDMAodlKAlxgEFJYoueHC7QYAX-KPLuzIw";

    public static final String WEB_API_BASE_URL = "http://mw.e4d.in/api/";
//    public static final String WEB_API_BASE_URL = "http://mw-uat.e4d.in/api/";

    public static final String API_USE_KEY = "F58386AD-EFB5-4843-8A12-D7D59BE6BEA0";
    public static final String API_USE_APPLICATION = "mobile";
    public static final String API_DEFAULT_USERID = "1";
    public static final String API_DEFAULT_USERNAME = "Uvtechglobal@gmail.com";
    public static final String API_DEFAULT_PASSWORD = "123456";

    public static final int REQUEST_PERMISSIONS_STORAGE = 0x16;

    public static final int REQUEST_PERMISSIONS_READ_CONTACTS = 0x17;
    public static final int REQUEST_PERMISSIONS_GOOGLE_LOGIN = 0x18;
    public static final int ON_ACTIVITY_RESULT_VIDEO_DETAIL_TO_DOWNLOAD = 0x38;

    public static final String FOLDER_NAME = Environment.getExternalStorageDirectory() + File.separator + "Video Downloader";
}

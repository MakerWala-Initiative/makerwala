package com.uvtech.makerwala;

import android.os.StrictMode;
import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.google.firebase.FirebaseApp;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.uvtech.makerwala.api.ApiCallingHelper;
import com.uvtech.makerwala.api.WebFields;
import com.uvtech.makerwala.api.WebLinks;
import com.uvtech.makerwala.helpers.LoginHelper;
import com.uvtech.makerwala.models.DownloadListModel;
import com.uvtech.makerwala.models.HomeListModel;

public class ApplicationLoader extends MultiDexApplication {

    private static ApplicationLoader applicationLoader = null;

    public static ApplicationLoader getInstance() {
        return applicationLoader;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MultiDex.install(this);
        applicationLoader = this;
        FirebaseApp.initializeApp(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(this, config);
    }

    private HomeListModel homeListModel = new HomeListModel();

    public HomeListModel getHomeListModel() {
        return homeListModel;
    }

    public void setHomeListModel(HomeListModel homeListModel) {
        this.homeListModel = homeListModel;
    }

    private String videoUrl = "", videoExt = "";

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoExt() {
        return videoExt;
    }

    public void setVideoExt(String videoExt) {
        this.videoExt = videoExt;
    }

    private JsonArray jsonArrayFilterCategory = new JsonArray();

    public JsonArray getJsonArrayFilterCategory() {
        return jsonArrayFilterCategory;
    }

    public void setJsonArrayFilterCategory(JsonArray jsonArrayFilterCategory) {
        this.jsonArrayFilterCategory = jsonArrayFilterCategory;
    }

    private JsonArray jsonArrayFilterSubCategory = new JsonArray();

    public JsonArray getJsonArrayFilterSubCategory() {
        return jsonArrayFilterSubCategory;
    }

    public void setJsonArrayFilterSubCategory(JsonArray jsonArrayFilterSubCategory) {
        this.jsonArrayFilterSubCategory = jsonArrayFilterSubCategory;
    }

    /* video play data pass from download screen to video play */

    private DownloadListModel downloadedModel = new DownloadListModel();

    public DownloadListModel getDownloadedModel() {
        return downloadedModel;
    }

    public void setDownloadedModel(DownloadListModel downloadedModel) {
        this.downloadedModel = downloadedModel;
    }

    public void callActivityLog(String type, String details) {
        JsonObject jsonObjectParam = new JsonObject();
        jsonObjectParam.addProperty(WebFields.KEY, ApplicationConstants.API_USE_KEY);
        if (!LoginHelper.Companion.getInstance().isLoggedIn())
            jsonObjectParam.addProperty(WebFields.USERNAME, ApplicationConstants.API_DEFAULT_USERNAME);
        else
            jsonObjectParam.addProperty(WebFields.USERNAME, LoginHelper.Companion.getInstance().getUserData().getTeacherEmailAddress());

        if (!LoginHelper.Companion.getInstance().isLoggedIn())
            jsonObjectParam.addProperty(WebFields.PASSWORD, ApplicationConstants.API_DEFAULT_PASSWORD);
        else
            jsonObjectParam.addProperty(WebFields.PASSWORD, LoginHelper.Companion.getInstance().getUserData().getPassword());

        if (!LoginHelper.Companion.getInstance().isLoggedIn())
            jsonObjectParam.addProperty(WebFields.USER_ID, ApplicationConstants.API_DEFAULT_USERID);
        else
            jsonObjectParam.addProperty(WebFields.USER_ID, LoginHelper.Companion.getInstance().getUserData().getId());

        jsonObjectParam.addProperty(WebFields.APPLICATION, ApplicationConstants.API_USE_APPLICATION);
        jsonObjectParam.addProperty(WebFields.TYPE, type);
        jsonObjectParam.addProperty(WebFields.DETAILS, details);
        jsonObjectParam.addProperty(WebFields.APP_VERSION, BuildConfig.VERSION_NAME);

        new ApiCallingHelper().callPostApi(this, WebLinks.GET_ACTIVITY_LOG, jsonObjectParam,
                null, false);
    }
}

package com.uvtech.makerwala.api;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.uvtech.makerwala.R;
import com.uvtech.makerwala.helpers.ConnectivityHelper;
import com.uvtech.makerwala.helpers.LogHelper;
import com.uvtech.makerwala.helpers.ToastHelper;
import okhttp3.MultipartBody;
import org.json.JSONObject;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.HashMap;
import java.util.List;

public class ApiCallingHelper {

    private boolean isLoading;
    private ApiCallingInterface apiCallingInterface = null;

    public synchronized void callMultipartApi(Context context, String webLink, List<MultipartBody.Part> parts,
                                              ApiCallingInterface apiCallingInterface) {
        callMultipartApi(context, webLink, parts, apiCallingInterface, true);
    }

    // is loading will show progressbar, api calling interface will pass data to activity
    public synchronized void callMultipartApi(final Context context, String webLink, List<MultipartBody.Part> parts,
                                              ApiCallingInterface apiCallingInterface, boolean isLoading) {
        if (!ConnectivityHelper.isConnectingToInternet()) {
            try {
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.INSTANCE.displayDialog(context, R.string.no_internet_connection);
                    }
                });
            } catch (Exception e) {
                LogHelper.INSTANCE.e(e.getMessage());
            }
            return;
        }

        this.isLoading = isLoading;
        this.apiCallingInterface = apiCallingInterface;
        if (this.isLoading)
            LoadingHelper.Companion.getInstance().show(context);

        Call<Object> call = RetrofitClient.INSTANCE.getClient()
                .multipart(webLink, parts);
        callingApi(call);
    }

    public synchronized void callPostApi(Context context, String webLink, JsonObject jsonObject,
                                         ApiCallingInterface apiCallingInterface) {
        callPostApi(context, webLink, jsonObject, apiCallingInterface, true);
    }

    // is loading will show progressbar, api calling interface will pass data to activity
    public synchronized void callPostApi(final Context context, String webLink, JsonObject jsonObject,
                                         ApiCallingInterface apiCallingInterface, boolean isLoading) {
        if (!ConnectivityHelper.isConnectingToInternet()) {
            try {
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.INSTANCE.displayDialog(context, R.string.no_internet_connection);
                    }
                });
            } catch (Exception e) {
                LogHelper.INSTANCE.e(e.getMessage());
            }
            return;
        }

        this.isLoading = isLoading;
        this.apiCallingInterface = apiCallingInterface;
        if (this.isLoading)
            LoadingHelper.Companion.getInstance().show(context);

        Call<Object> call = RetrofitClient.INSTANCE.getClient()
                .post(webLink, jsonObject);
        callingApi(call);
    }

    public synchronized void callPostApi(Context context, String webLink, HashMap<String, String> map,
                                         ApiCallingInterface apiCallingInterface) {
        callPostApi(context, webLink, map, apiCallingInterface, true);
    }

    // is loading will show progressbar, api calling interface will pass data to activity
    public synchronized void callPostApi(final Context context, String webLink, HashMap<String, String> map,
                                         ApiCallingInterface apiCallingInterface, boolean isLoading) {
        if (!ConnectivityHelper.isConnectingToInternet()) {
            try {
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.INSTANCE.displayDialog(context, R.string.no_internet_connection);
                    }
                });
            } catch (Exception e) {
                LogHelper.INSTANCE.e(e.getMessage());
            }
            return;
        }

        this.isLoading = isLoading;
        this.apiCallingInterface = apiCallingInterface;
        if (this.isLoading)
            LoadingHelper.Companion.getInstance().show(context);

        Call<Object> call = RetrofitClient.INSTANCE.getClient()
                .post(webLink, map);
        callingApi(call);
    }

    public synchronized void callGetApi(Context context, String webLink,
                                        ApiCallingInterface apiCallingInterface) {
        callGetApi(context, webLink, apiCallingInterface, true);
    }

    // is loading will show progressbar, api calling interface will pass data to activity
    private synchronized void callGetApi(final Context context, String webLink,
                                         ApiCallingInterface apiCallingInterface, boolean isLoading) {
        if (!ConnectivityHelper.isConnectingToInternet()) {
            try {
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.INSTANCE.displayDialog(context, R.string.no_internet_connection);
                    }
                });
            } catch (Exception e) {
                LogHelper.INSTANCE.e(e.getMessage());
            }
            return;
        }

        ApiCallingHelper.this.isLoading = isLoading;
        ApiCallingHelper.this.apiCallingInterface = apiCallingInterface;
        if (ApiCallingHelper.this.isLoading)
            LoadingHelper.Companion.getInstance().show(context);

        Call<Object> call = RetrofitClient.INSTANCE.getClient()
                .get(webLink);
        callingApi(call);
    }

    private void callingApi(Call<Object> call) {
        call.enqueue(new Callback<Object>() {
            @Override
            public void onResponse(@NonNull Call<Object> call, @NonNull Response<Object> response) {
                if (ApiCallingHelper.this.isLoading)
                    LoadingHelper.Companion.getInstance().dismiss();
                try {
                    String strRespResult = new Gson().toJson(response.body());
                    JSONObject jsonObject = new JSONObject(strRespResult);
                    if (apiCallingInterface != null)
                        if (jsonObject.getInt(WebFields.STATUS) == 1)
                            ApiCallingHelper.this.apiCallingInterface.onSuccess(strRespResult, jsonObject.getString(WebFields.MESSAGE));
                        else
                            ApiCallingHelper.this.apiCallingInterface.onFailure(jsonObject.getString(WebFields.MESSAGE));
                } catch (Exception e) {
                    if (apiCallingInterface != null)
                        ApiCallingHelper.this.apiCallingInterface.onFailure("Failed To Load");
                }
            }

            @Override
            public void onFailure(@NonNull Call<Object> call, @NonNull Throwable t) {
                if (ApiCallingHelper.this.isLoading)
                    LoadingHelper.Companion.getInstance().dismiss();
                if (apiCallingInterface != null) {
                    ApiCallingHelper.this.apiCallingInterface.onFailure("Failed To Load");
                }
            }
        });
    }
}

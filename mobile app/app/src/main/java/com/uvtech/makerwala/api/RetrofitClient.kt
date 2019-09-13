package com.uvtech.makerwala.api

import com.uvtech.makerwala.ApplicationConstants
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder

object RetrofitClient {

    private var retrofit: Retrofit? = null
    var gson = Gson()

    val client: RetrofitInterface
        get() {

            if (retrofit == null) {
                val logging = HttpLoggingInterceptor()
                logging.level = HttpLoggingInterceptor.Level.BODY
                val httpClient = OkHttpClient.Builder()
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS)
                        .connectTimeout(60, TimeUnit.SECONDS)
                httpClient.addInterceptor(logging)
                val gson = GsonBuilder()
                        .setLenient()
                        .create()
                retrofit = Retrofit.Builder()
                        .baseUrl(ApplicationConstants.WEB_API_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .client(httpClient.build())
                        .build()
            }
            return retrofit!!.create(RetrofitInterface::class.java)
        }
}

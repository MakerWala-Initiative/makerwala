package com.uvtech.makerwala.api

import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.util.*

interface RetrofitInterface {

    @Headers("Content-Type: application/json")
    @GET
    operator fun get(@Url url: String): Call<Any>

    @Headers("Content-Type: application/json")
    @POST
    fun post(@Url url: String, @Body jsonObject: JsonObject): Call<Any>

    @POST
    fun post(@Url url: String, @Body map: HashMap<String, String>): Call<Any>

    @Multipart
    @POST
    fun multipart(@Url url: String, @PartMap map: HashMap<String, Any>, @Part multipart: MultipartBody.Part): Call<Any>

    @Multipart
    @POST
    fun multipart(@Url url: String, @Part multipart: List<MultipartBody.Part>): Call<Any>
}

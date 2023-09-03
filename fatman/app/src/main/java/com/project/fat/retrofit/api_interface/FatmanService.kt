package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.UserFatman
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path

interface FatmanService {
    @PUT("userfatman")
    fun addUserFatman(
        @Header("Access-Token") accessToken : String,
        @Path("fatmanId") fatmanId : Long)

    @GET("userfatman")
    fun getUserFatman(
        @Header("Access-Token") accessToken : String) : Call<UserFatman>
}
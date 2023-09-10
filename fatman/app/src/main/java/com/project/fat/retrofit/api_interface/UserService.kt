package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.AuthorizeResponse
import com.project.fat.data.dto.SocialLoginRequest
import com.project.fat.data.dto.SocialLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserService {
    @POST("users")
    fun socialLogin(
        @Body token : SocialLoginRequest
    ) : Call<SocialLoginResponse>

    @GET("users/refresh")
    fun authorize(
        @Header("Refresh-Token") refreshToken : String,
        @Header("Access-Token") accessToken : String
    ) : Call<AuthorizeResponse>
}
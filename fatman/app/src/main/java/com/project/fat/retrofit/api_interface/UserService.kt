package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.SocialLoginRequest
import com.project.fat.data.dto.SocialLoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("users")
    fun socialLogin(
        @Body token : SocialLoginRequest
    ) : Call<SocialLoginResponse>
}
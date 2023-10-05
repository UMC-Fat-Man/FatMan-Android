package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.ErrorResponse
import com.project.fat.data.dto.Monster
import com.project.fat.data.dto.AddUserMonsterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface UserMonsterService {
    @GET("user_monster")
    fun getUserMonster(
        @Header("Access-Token") accessToken : String
    ) : Call<ArrayList<Monster>>

    @POST("user_monster")
    fun addUserMonster(
        @Header("Access-Token") accessToken : String,
        @Body addUserMonsterRequest: AddUserMonsterRequest
    ) : Call<ErrorResponse>
}
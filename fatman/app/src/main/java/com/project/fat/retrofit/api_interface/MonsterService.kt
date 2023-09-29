package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.Monster
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface MonsterService {
    @GET("user_monster")
    fun getUserMonster(
        @Header("Access-Token") accessToken : String
    ) : Call<ArrayList<Monster>>

    @POST("user_monster")
    fun addUserMonster(
        @Header("Access-Token") accessToken : String,
        @Body monster_id : Long
    )
}
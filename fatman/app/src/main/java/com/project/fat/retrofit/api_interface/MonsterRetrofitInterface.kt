package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.Monster
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST

interface MonsterRetrofitInterface {
    @GET
    fun getUserMonster() : MutableList<Call<Monster>>

    @POST
    fun addUserMonster() : Call<Monster>
}
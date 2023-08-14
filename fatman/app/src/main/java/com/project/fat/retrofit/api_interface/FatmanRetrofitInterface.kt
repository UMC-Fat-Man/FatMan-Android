package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.Fatman
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface FatmanRetrofitInterface {
    @PUT
    fun addUserFatman(
        @Query("fatmanId") fatmanId : Long) : Call<Fatman>

    @GET
    fun getUserFatman(
        @Query("userId") userId : Long) : MutableList<Call<Fatman>>
}
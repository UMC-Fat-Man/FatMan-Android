package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.CreateHistoryResponse
import com.project.fat.data.dto.GetHistoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface HistoryService {
    @GET("history")
    fun getHistory(
        @Header("access_token") accessToken : String
    ) : Call<GetHistoryResponse>

    @POST("history")
    fun createHistory(
        @Header("Access-Token") accessToken : String,
        @Body monsterNum : Int,
        @Body distance : Double,
        @Body date : String
    ) :Call<CreateHistoryResponse>

    @DELETE("history")
    fun deleteHistory(
        @Path("id") id : Long
    )
}
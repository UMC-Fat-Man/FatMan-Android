package com.project.fat.retrofit.api_interface

import com.project.fat.data.dto.CreateHistoryRequest
import com.project.fat.data.dto.CreateHistoryResponse
import com.project.fat.data.dto.GetHistoryResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import java.time.LocalDate
import java.time.LocalDateTime

interface HistoryService {
    @GET("history")
    fun getHistory(
        @Header("access_token") accessToken: String,
        @Body targetDate: LocalDate
    ) : Call<GetHistoryResponse>

    @POST("history")
    fun createHistory(
        @Header("access_token") accessToken : String,
        @Body createHistoryRequest: CreateHistoryRequest
    ) :Call<CreateHistoryResponse>

    @DELETE("history")
    fun deleteHistory(
        @Path("id") id : Long
    )
}
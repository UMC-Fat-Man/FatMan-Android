package com.project.fat.retrofit.api_interface

<<<<<<< Updated upstream
import com.project.fat.data.dto.UserFatman
=======
import com.project.fat.data.dto.Fatman
>>>>>>> Stashed changes
import retrofit2.Call
import retrofit2.http.GET

interface FatmanService {
<<<<<<< Updated upstream
    @PUT("userfatman")
    fun addUserFatman(
        @Header("Access-Token") accessToken : String,
        @Path("fatmanId") fatmanId : Long)

    @GET("userfatman")
    fun getUserFatman(
        @Header("Access-Token") accessToken : String) : Call<UserFatman>
=======
    @GET("fatman")
    fun getFatmanList() : Call<Fatman>
>>>>>>> Stashed changes
}
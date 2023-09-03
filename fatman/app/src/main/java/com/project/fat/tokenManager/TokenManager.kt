package com.project.fat.tokenManager

import android.util.Log
import com.project.fat.R
import com.project.fat.data.dto.AuthorizeResponse
import com.project.fat.retrofit.client.UserRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object TokenManager {
    private var accessToken : String? = null
    private var refreshToken : String? = null

    fun setToken(accessToken : String, refreshToken : String){
        this.accessToken = accessToken
        this.refreshToken = refreshToken
    }

    fun getAccessToken() = accessToken

    fun getRefreshToken() = refreshToken

    fun authorize(
        accessToken: String,
        refreshToken: String,
        prefixOfAccessToken : String,
        prefixOfRefreshToken : String,
        callback: (Boolean) -> Unit) {

        if(this.accessToken == null || this.refreshToken == null) {
            this.accessToken = accessToken
            this.refreshToken = refreshToken
        }

        UserRetrofit.getApiService()!!.authorize(prefixOfRefreshToken+refreshToken, prefixOfAccessToken+accessToken)
            .enqueue(object : Callback<AuthorizeResponse>{
                override fun onResponse(
                    call: Call<AuthorizeResponse>,
                    response: Response<AuthorizeResponse>
                ) {
                    if (response.isSuccessful) {
                        val result = response.headers()
                        val backendApiAccessToken = result["Access-Token"]
                        val backendApiRefreshToken = result["Refresh-Token"]
                        Log.d(
                            "BackEnd API Authorize Success",
                            "accessToken : $backendApiAccessToken\nrefreshToken : $backendApiRefreshToken"
                        )
                        this@TokenManager.accessToken = backendApiAccessToken!!.replace(prefixOfAccessToken, "")
                        this@TokenManager.refreshToken = backendApiRefreshToken!!.replace(prefixOfRefreshToken, "")

                        Log.d("TokenManger", "TokenManger.accessToken : ${this@TokenManager.accessToken}\nTokenManager.refreshToken : ${this@TokenManager.refreshToken}")
                        callback(true)
                    }
                    else{
                        Log.d("BackEnd API Authorize response not successful", "Error : ${response.code()}")
                        callback(false)
                    }
                }

                override fun onFailure(call: Call<AuthorizeResponse>, t: Throwable) {
                    Log.d("BackEnd API Authorize Failure", "Fail : ${t.printStackTrace()}\n Error message : ${t.message}")
                    callback(false)
                }

            })
    }
}
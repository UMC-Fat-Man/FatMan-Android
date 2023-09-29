package com.project.fat.retrofit

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.dataStore
import com.project.fat.data.dto.SignInRequest
import com.project.fat.data.dto.SignInResponse
import com.project.fat.data.dto.getUserResponse
import com.project.fat.data.dto.updateUserDetailRequest
import com.project.fat.data.dto.updateUserDetailResponse
import com.project.fat.dataStore.UserDataStore
import com.project.fat.retrofit.client.UserRetrofit
import com.project.fat.tokenManager.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class LoginRepository {
    var loginApiService = UserRetrofit.getApiService()



    fun deleteUser(){
        loginApiService?.deleteUser(accessToken = TokenManager.getAccessToken()!!)
    }


}
package com.project.fat.retrofit

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import androidx.datastore.dataStore
import com.project.fat.data.dto.SignInRequest
import com.project.fat.data.dto.SignInResponse
import com.project.fat.data.dto.updateUserDetailRequest
import com.project.fat.data.dto.updateUserDetailResponse
import com.project.fat.dataStore.UserDataStore
import com.project.fat.retrofit.client.UserRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class LoginRepository {
    var loginApiService = UserRetrofit.getApiService()

    fun signUp(
        email: String,
        name: String,
        password: Int,
        nickname: String,
        address: String,
        birth: String,
        state: String
    ){
        loginApiService?.signUp(email,name,password,nickname,address,birth,state)
    }
    fun signIn(email: String, password: String){
        lateinit var signIn : SignInRequest
        signIn.email = email
        signIn.password = password

        loginApiService?.signIn(signIn)?.enqueue(object : Callback<SignInResponse> {
            override fun onResponse(
                call: Call<SignInResponse>,
                response: Response<SignInResponse>
            ) {
                if(response.isSuccessful){
                    val result = response.body()!!
                    val id = result.id
                    val email = result.email
                    val name = result.name
                    val nickname = result.nickname
                    Log.d(TAG, "Id: $id" +
                                "\nEmail: $email" +
                                "\nName: $name" +
                                "\nNickName: $nickname"
                    )

                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
    }
    fun updateUserDetail2(nickname: String, address: String, birth: String){
        lateinit var update: updateUserDetailRequest
        loginApiService?.updateUserDetail2(accessToken = UserDataStore.ACCESS_TOKEN.toString(), update)
            ?.enqueue(object : Callback<updateUserDetailResponse>{
                override fun onResponse(
                    call: Call<updateUserDetailResponse>,
                    response: Response<updateUserDetailResponse>
                ) {
                    if(response.isSuccessful){
                        val result = response.body()!!
                        val email = result.email
                        val name = result.name
                        val nickname = result.nickname

                        Log.d(TAG, "Email: $email" +
                                "\nName: $name" +
                                "\nNickName: $nickname"
                        )
                    }
                }

                override fun onFailure(call: Call<updateUserDetailResponse>, t: Throwable) {
                    Log.e(ContentValues.TAG, "getOnFailure: ",t.fillInStackTrace())
                }

            })
    }
    fun deleteUser(){
        loginApiService?.deleteUser(accessToken = UserDataStore.ACCESS_TOKEN.toString())
    }
    fun getUser(){
        loginApiService?.getUser(accessToken = UserDataStore.ACCESS_TOKEN.toString())
    }

}
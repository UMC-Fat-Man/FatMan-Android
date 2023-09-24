package com.project.fat

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.project.fat.data.dto.SignInRequest
import com.project.fat.data.dto.SignInResponse
import com.project.fat.dataStore.UserDataStore
import com.project.fat.dataStore.UserDataStore.dataStore
import com.project.fat.databinding.ActivitySignInBinding
import com.project.fat.retrofit.client.UserRetrofit
import com.project.fat.tokenManager.TokenManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignInActivity : AppCompatActivity() {
    var loginApiService = UserRetrofit.getApiService()
    var nickname: String? = null
    var money: Int? = null
    lateinit var binding: ActivitySignInBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        binding.signInBtn.setOnClickListener{
            val email = binding.signInEmail.toString()
            val password = binding.signInPassword.toString()

            signIn(email, password)

            moveActivity()
        }
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
                    val accessToken = response.headers()["Access-Token"].toString()
                    val refreshToken = response.headers()["Refresh-Token"].toString()

                    val id = result.id
                    val email = result.email
                    val name = result.name
                    nickname = result.nickname
                    money = result.money

                    Log.d(
                        ContentValues.TAG, "Id: $id" +
                            "\nEmail: $email" +
                            "\nName: $name" +
                            "\nNickName: $nickname"
                    )
                    saveToken(accessToken,refreshToken)

                }
            }

            override fun onFailure(call: Call<SignInResponse>, t: Throwable) {
                Log.e(ContentValues.TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
    }
    fun moveActivity(){
        val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
        intent.putExtra("nickname", nickname)
        intent.putExtra("money",money)
        startActivity(intent)
        finish()
    }


    private fun saveToken(accessToken : String, refreshToken : String){ //access token과 refresh token을 dataStore에 저장
        lifecycleScope.launch {
            Log.d("saveToken in dataStore", "start")
            Log.d("saveToken", " context.dataStore = ${this@SignInActivity?.dataStore}")
            this@SignInActivity.dataStore.edit {
                Log.d("saveToken in dataStore", "start")
                it[UserDataStore.ACCESS_TOKEN] = accessToken
                Log.d("saveToken in dataStore", "accessToken saved")
                it[UserDataStore.REFRESH_TOKEN] = refreshToken
                Log.d("saveToken in dataStore", "refreshToken saved end")
            }

            TokenManager.setToken(accessToken, refreshToken)
            Log.d("saveToken in dataStore", "end")
        }
    }
}
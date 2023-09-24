package com.project.fat

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.project.fat.data.dto.updateUserDetailRequest
import com.project.fat.data.dto.updateUserDetailResponse
import com.project.fat.dataStore.UserDataStore
import com.project.fat.databinding.ActivityAdditionalInfoBinding
import com.project.fat.retrofit.client.UserRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdditionalInfoActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdditionalInfoBinding

    var loginApiService = UserRetrofit.getApiService()
    var nickname: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_additional_info)

        binding = ActivityAdditionalInfoBinding.inflate(layoutInflater)

        binding.addInfoBtn.setOnClickListener {
            nickname = binding.addInfoNickname.toString()
            val address = binding.addInfoAddress.toString()
            val birth = binding.addInfoBirth.toString()

            updateUserDetail2(nickname!!, address,birth)

        }

    }
    fun updateUserDetail2(nickname: String, address: String, birth: String){
        val update = updateUserDetailRequest(nickname, address, birth)

        loginApiService?.updateUserDetail2(accessToken = UserDataStore.ACCESS_TOKEN.toString(), update)
            ?.enqueue(object : Callback<updateUserDetailResponse> {
                override fun onResponse(
                    call: Call<updateUserDetailResponse>,
                    response: Response<updateUserDetailResponse>
                ) {
                    if(response.isSuccessful){
                        val result = response.body()!!
                        val email = result.email
                        val name = result.name
                        val nickname = result.nickname

                        Log.d(
                            ContentValues.TAG, "Email: $email" +
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

    fun moveActivity(){
        val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
        intent.putExtra("nickname", nickname)
        startActivity(intent)
        finish()

    }
}
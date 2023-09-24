package com.project.fat

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.project.fat.databinding.ActivitySignUp2Binding
import com.project.fat.databinding.ActivitySignUpBinding
import com.project.fat.retrofit.client.UserRetrofit
import retrofit2.Call
import retrofit2.Response
import javax.security.auth.callback.Callback

class SignUp2Activity : AppCompatActivity() {
    lateinit var binding: ActivitySignUp2Binding

    var loginApiService = UserRetrofit.getApiService()

    var loginState: String? = "ROLE_USER"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up2)

        binding = ActivitySignUp2Binding.inflate(layoutInflater)

        binding.signUpBtn.setOnClickListener {
            val userName = binding.signUpUsername.toString()
            val nickname = binding.signUpNickname.toString()
            val address = binding.signUpAddress.toString()
            val birth = binding.signUpBirth.toString()
            val email = intent.getStringExtra("email")
            val password = intent.getStringExtra("password")

            signUp(email!!, userName, password, nickname, address, birth, loginState)
        }
    }

    fun moveActivity(){
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }



    fun signUp(
        email: String,
        name: String?,
        password: String?,
        nickname: String?,
        address: String,
        birth: String,
        state: String?
    ){
        loginApiService?.signUp(email, name!!,password!!,nickname!!,address,birth,state!!)!!.enqueue(object: retrofit2.Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful){
                    val message = response.body()
                    Log.d(TAG, "회원가입 성공: $message")
                    moveActivity()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                Log.e(ContentValues.TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
    }
}
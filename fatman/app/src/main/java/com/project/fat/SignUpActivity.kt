package com.project.fat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.fat.databinding.ActivitySignUpBinding
import com.project.fat.googleLoginAccessToken.LoginRepository
import com.project.fat.retrofit.client.UserRetrofit

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    var userName: String? = null
    var password: String? = null
    var loginState: String? = null
    var nickname: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)

        binding.signUpBtn.setOnClickListener {
            nickname = binding.signUpNickname.toString()
            val address = binding.signUpAddress.toString()
            val birth = binding.signUpBirth.toString()
            val email = binding.signUpEmail.toString()

            signUp(email, userName, password,nickname,address,birth, loginState)
            moveActivity()

        }
    }

    fun moveActivity(){
        val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
        intent.putExtra("username", userName)
        intent.putExtra("nickname", nickname)
        startActivity(intent)
        finish()
    }

    var loginApiService = UserRetrofit.getApiService()

    fun signUp(
        email: String,
        name: String?,
        password: String?,
        nickname: String?,
        address: String,
        birth: String,
        state: String?
    ){
        loginApiService?.signUp(email, name!!,password!!,nickname!!,address,birth,state!!)
    }
}
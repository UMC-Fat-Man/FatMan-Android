package com.project.fat

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.project.fat.databinding.ActivitySignUpBinding
import com.project.fat.retrofit.client.UserRetrofit
import retrofit2.Call
import retrofit2.Response

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    var loginApiService = UserRetrofit.getApiService()

    var email: String? = null
    var password: String? = null
    var password_check: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding.signUpBtn.setOnClickListener {
            email = binding.signUpEmail.toString()
            password = binding.signUpPassword.toString()
            password_check = binding.signUpPassword2.toString()

            if (password == password_check)
                moveActivity()
            else
                Toast.makeText(this, "비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()

        }
    }

    fun moveActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.putExtra("email", email)
        intent.putExtra("password", password)
        startActivity(intent)
        finish()
    }




}
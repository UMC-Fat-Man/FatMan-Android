package com.project.fat.fragment.bottomNavigationActivity

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.fat.BottomNavigationActivity
import com.project.fat.R
import com.project.fat.databinding.FragmentSettingsBinding
import com.project.fat.LoginActivity
import com.project.fat.tokenManager.TokenManager

class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)


        binding.btnLogout.setOnClickListener {
            if(LoginActivity().google_user != null) {
                googleLogout()
                TokenManager.logout()
                moveLoginActivity()
            }
            else {
                moveLoginActivity()
            }
        }

        return binding.root
    }

    private fun moveLoginActivity(){
        val intent = Intent(context, LoginActivity()::class.java)
        intent.putExtra("logoutState", false)
        startActivity(intent)
    }

    //로그아웃
    
    private fun googleLogout() {
        val intent = Intent(context, LoginActivity()::class.java)
        intent.putExtra("logoutState", false)
        startActivity(intent)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
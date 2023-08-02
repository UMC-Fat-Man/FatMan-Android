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
import com.gorisse.thomas.lifecycle.getActivity
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.project.fat.BottomNavigationActivity
import com.project.fat.R
import com.project.fat.databinding.FragmentSettingsBinding
import com.project.fat.LoginActivity

class SettingsFragment : Fragment() {
    private var _binding : FragmentSettingsBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)


        binding.btnLogout.setOnClickListener {
            if(LoginActivity().kakao_user != null){
                kakaoLogout()
                kakaoUnlink()
                moveLoginActivity()
            }
            else if(LoginActivity().naver_user != null) {
                NaverIdLoginSDK.logout()
                moveLoginActivity()
            }
            else if(LoginActivity().google_user != null) {
                googleLogout()
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
    private fun kakaoLogout(){
        UserApiClient.instance.logout { error ->
            if(error != null){
                Log.d(TAG, "로그아웃 실패" ,null)
            }
            else{
                Log.d(TAG, "로그아웃 성공, SDK에서 토큰 삭제 됨" ,null)
            }
        }
    }
    //연결 끊기
    private fun kakaoUnlink(){
        UserApiClient.instance.unlink { error ->
            if(error != null){
                Log.d(TAG, "연결 끊기 실패" ,null)
            }
            else{
                Log.d(TAG, "연결 끊기 성공, SDK에서 토큰 삭제 됨" ,null)
            }
        }
    }
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
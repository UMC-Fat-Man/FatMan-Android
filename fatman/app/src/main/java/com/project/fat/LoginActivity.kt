package com.project.fat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.project.fat.BuildConfig.naver_client_id
import com.project.fat.BuildConfig.kakaoLoginKey
import com.project.fat.BuildConfig.naver_client_secret

import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.project.fat.databinding.ActivityLoginBinding
import java.util.Arrays


class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    lateinit var user_id: String
    lateinit var callbackManager: CallbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)

        KakaoSdk.init(this, kakaoLoginKey)
        NaverIdLoginSDK.initialize(baseContext,naver_client_id,naver_client_secret,"네이버 로그인")
        //Log.d(TAG, "keyhash ${Utility.getKeyHash(this)}")
        //DP1zQOa5mjnTte/hgypxWr7Llig=



        loginBinding.kakaoLogin.setOnClickListener {
            kakaoLogin()
        }
        loginBinding.naverLogin.setOnClickListener{
            naverLogin()
        }

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Pass the activity result back to the Facebook SDK
        callbackManager.onActivityResult(requestCode, resultCode, data)
    }
    override fun onDestroy() {
        super.onDestroy()
        kakaoLogout()
        kakaoUnlink()
        NaverIdLoginSDK.logout()

    }

    private fun kakaoLogin(){
        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if(error != null){

                Log.e(TAG, "토큰 에러 $error")
            }
            else if (tokenInfo != null) {

                Log.e(TAG, "토큰 정보 $tokenInfo")
                finish()
            }
            // 카카오계정으로 로그인 공통 callback 구성
            // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
            val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                if(error != null){

                    Log.e(TAG, "기타 에러 $error")
                }
                else if(token != null){

                    Log.e(TAG, "토큰 $tokenInfo")
                    finish()
                }
            }
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(this)){
                UserApiClient.instance.loginWithKakaoTalk(this){ token, error ->
                    if (error != null) {

                        Log.e(TAG, "토큰 에러 카톡 로그인 $error")
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }

                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {

                        Log.e(TAG, "토큰 카카오계정 로그인 $tokenInfo")
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
        //사용자 정보 가져오기
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            }
            else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공" +
                        "\n회원번호: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
        }
    }

    //로그아웃
    private fun kakaoLogout(){
        UserApiClient.instance.logout { error ->
            if(error != null){
                Toast.makeText(this, "로그아웃 실패" ,Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "로그아웃 성공, SDK에서 토큰 삭제 됨" ,Toast.LENGTH_SHORT).show()
            }
        }
    }
    //연결 끊기
    private fun kakaoUnlink(){
        UserApiClient.instance.unlink { error ->
            if(error != null){
                Toast.makeText(this, "연결 끊기 실패" ,Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(this, "연결 끊기 성공, SDK에서 토큰 삭제 됨" ,Toast.LENGTH_SHORT).show()
            }
        }
    }

    //naver login
    private fun naverLogin(){
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        user_id = result.profile?.id.toString()
                        Log.e(TAG, "네이버 로그인한 유저 정보 - 아이디 : $user_id")

                        val accessToken = NaverIdLoginSDK.getAccessToken()

                        Log.d("test", "AccessToken : " + accessToken)
                        Log.d("test", "client id : " + naver_client_id)
                        Log.d("test", "ReFreshToken : " + NaverIdLoginSDK.getRefreshToken())
                        Log.d("test", "Expires : " + NaverIdLoginSDK.getExpiresAt().toString())
                        Log.d("test", "TokenType : " + NaverIdLoginSDK.getTokenType())
                        Log.d("test", "State : " + NaverIdLoginSDK.getState().toString())
                    }

                    override fun onError(errorCode: Int, message: String) {

                    }

                    override fun onFailure(httpStatus: Int, message: String) {

                    }
                })
            }
            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("test", "$errorCode $errorDescription")
            }
            override fun onError(errorCode: Int, message: String) {
                //onFailure(errorCode, message)
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("test", "$errorCode $errorDescription")
            }
        }

        NaverIdLoginSDK.authenticate(this, oauthLoginCallback)
    }



}

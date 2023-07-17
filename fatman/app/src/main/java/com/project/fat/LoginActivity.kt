package com.project.fat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.project.fat.BuildConfig.naver_client_id
import com.project.fat.BuildConfig.kakaoLoginKey
import com.project.fat.BuildConfig.naver_client_secret
import com.project.fat.BuildConfig.google_client_id

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
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
    lateinit var user_id: String

    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private lateinit var startGoogleLoginForResult : ActivityResultLauncher<Intent>
    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)

        KakaoSdk.init(this, kakaoLoginKey)
        NaverIdLoginSDK.initialize(baseContext,naver_client_id,naver_client_secret,"네이버 로그인")
        //Log.d(TAG, "keyhash ${Utility.getKeyHash(this)}")
        //DP1zQOa5mjnTte/hgypxWr7Llig=
        getGoogleClient()



        loginBinding.kakaoLogin.setOnClickListener {
            kakaoLogin()
        }
        loginBinding.naverLogin.setOnClickListener{
            naverLogin()
        }
        loginBinding.googleLogin.setOnClickListener {
            googleLogin()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoLogout()
        kakaoUnlink()
        NaverIdLoginSDK.logout()
        googleLogout()

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
                Log.i(TAG, "카카오 로그인 사용자 정보 요청 성공" +
                        "\n회원아이디: ${user.id}" +
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
                        Log.e(TAG, "네이버 로그인한 유저 정보 - 아이디 : $user_id" +
                                "\n이메일: ${result.profile?.email}" +
                                "\n닉네임: ${result.profile?.nickname}")

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

    //google login
    private fun googleLogin() {
        //GoogleSignInClient.signOut()

        val signInIntent = mGoogleSignInClient.signInIntent
        startGoogleLoginForResult.launch(signInIntent)
    }
    private fun getGoogleClient() {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(google_client_id) // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOption)

        startGoogleLoginForResult =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
                if (result.resultCode == RESULT_OK) {
                    result.data?.let { data ->

                        val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

                        try {
                            // Google Sign In was successful, authenticate with Firebase, 사용자 정보 가져오기
                            val account = task.getResult(ApiException::class.java)!!

                            user_id = account.id.toString()
                            Log.d(TAG, "카카오 로그인 사용자 정보 요청 성공" +
                                    "\n회원아이디: ${user_id}" +
                                    "\n이메일: ${account.email}" +
                                    "\n닉네임: ${account.familyName + account.givenName} ")
                            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                            firebaseAuthWithGoogle(account.idToken!!)
                        } catch (e: ApiException) {
                            // Google Sign In failed, update UI appropriately
                            Log.w(TAG, "Google sign in failed", e)
                        }
                    }
                    // Google Login Success
                } else {
                    Log.e(TAG, "Google Result Error ${result}")
                }
            }

    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)

                }
            }
    }
   private fun googleLogout(){
       mGoogleSignInClient.signOut()
           .addOnCompleteListener(this) {
               // 로그아웃 성공시 실행
               // 로그아웃 이후의 이벤트들(토스트 메세지, 화면 종료)을 여기서 수행하면 됨
           }
   }
}

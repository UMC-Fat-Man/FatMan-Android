package com.project.fat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.activity.result.contract.ActivityResultContracts
/*import com.project.fat.BuildConfig.naver_client_id
import com.project.fat.BuildConfig.kakaoLoginKey
import com.project.fat.BuildConfig.naver_client_secret*/
//import com.project.fat.BuildConfing

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope

import com.google.android.gms.tasks.Task
import com.google.android.material.bottomnavigation.BottomNavigationMenuView

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



class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    var kakao_user: String? = null
    var naver_user: String? = null
    var google_user: String? = null
    var loginState: Boolean = true
    var userName: String? = null
    val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    var gsa: GoogleSignInAccount? = null
    private val googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

        try {
            val account = task.getResult(ApiException::class.java)

            // 이름, 이메일 등이 필요하다면 아래와 같이 account를 통해 각 메소드를 불러올 수 있다.
            userName = account.familyName + account.givenName
            val serverAuth = account.serverAuthCode
            google_user = account.email

            Log.d(TAG, "구글 로그인 사용자 정보 요청 성공" +
                    "\n회원아이디: ${google_user}" +
                    "\n서버 인증 코드: ${account.serverAuthCode}" +
                    "\n토큰: ${account.idToken}" +
                    "\n이메일: ${account.email}" +
                    "\n닉네임: ${userName} ")

            moveSignUpActivity()

        } catch (e: ApiException) {
            //Log.e(SignFragment::class.java.simpleName, e.stackTraceToString())
            Log.d(TAG,"에러 : $e")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)

        //KakaoSdk.init(this, kakaoLoginKey)
        //NaverIdLoginSDK.initialize(baseContext,naver_client_id,naver_client_secret,"네이버 로그인")
        //Log.d(TAG, "keyhash ${Utility.getKeyHash(this)}")
        //DP1zQOa5mjnTte/hgypxWr7Llig=
        //getGoogleClient()

        loginState = intent.getBooleanExtra("logoutState" , true)

        if(loginState == false){  //설정 화면에서 로그아웃 버튼을 누르면 false 값이 전달됨
            googleSignInClient.signOut().addOnCompleteListener{
                Toast.makeText(this,"로그아웃 되었습니다",Toast.LENGTH_SHORT).show()
                Log.d(TAG,"로그아웃 후 닉네임: $userName")
            }
            googleSignInClient.revokeAccess().addOnCompleteListener{

            }
        }

        loginBinding.kakaoLogin.setOnClickListener {
            kakaoLogin()
        }
        loginBinding.naverLogin.setOnClickListener{
            naverLogin()
        }
        /*loginBinding.googleLogin.setOnClickListener {
            googleLogin()
        }*/
    }

    override fun onStart() {  // 로그인 된 기록이 있는 경우 바로 홈 화면으로 넘어감
        super.onStart()
        gsa = GoogleSignIn.getLastSignedInAccount(this)

        if(gsa != null){
            userName = gsa!!.familyName + gsa!!.givenName
            Log.d(TAG, "이미 로그인 됨 " + gsa?.email.toString())
            Toast.makeText(this,"로그인 되었습니다",Toast.LENGTH_SHORT).show()
            moveSignUpActivity()
        }
        else{
            Toast.makeText(this,"로그인 해야합니다.",Toast.LENGTH_SHORT).show()

        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
                kakao_user = user.id.toString()
                Log.i(TAG, "카카오 로그인 사용자 정보 요청 성공" +
                        "\n회원아이디: ${user.id}" +
                        "\n이메일: ${user.kakaoAccount?.email}" +
                        "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                        "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}")
            }
        }
    }

    //naver login
    private fun naverLogin(){
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                NidOAuthLogin().callProfileApi(object : NidProfileCallback<NidProfileResponse> {
                    override fun onSuccess(result: NidProfileResponse) {
                        naver_user = result.profile?.id.toString()
                        Log.e(TAG, "네이버 로그인한 유저 정보 - 아이디 : $naver_user" +
                                "\n이메일: ${result.profile?.email}" +
                                "\n닉네임: ${result.profile?.nickname}")

                        val accessToken = NaverIdLoginSDK.getAccessToken()

                        Log.d("test", "AccessToken : " + accessToken)
                        //Log.d("test", "client id : " + naver_client_id)
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
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestProfile()
            //.requestServerAuthCode(google_client_id) // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            //.requestEmail() // 이메일도 요청
            //.requestIdToken(google_client_id)
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun moveSignUpActivity() {
        val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
        intent.putExtra("username", google_user)
        intent.putExtra("nickname",userName)
        startActivity(intent)
        finish()
    }

}

package com.project.fat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.project.fat.BuildConfig.google_client_id
import com.project.fat.data.dto.SocialLoginRequest
import com.project.fat.data.dto.SocialLoginResponse
import com.project.fat.dataStore.UserDataStore
import com.project.fat.dataStore.UserDataStore.dataStore
import com.project.fat.databinding.ActivityLoginBinding
import com.project.fat.googleLoginAccessToken.LoginRepository
import com.project.fat.retrofit.client.UserRetrofit
import com.project.fat.tokenManager.TokenManager
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding

    private lateinit var callSocialLogin : Call<SocialLoginResponse>

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
            if (account.familyName == null) {
                userName = account.givenName
            } else if (account.givenName == null) {
                userName = account.familyName
            } else {
                userName = account.familyName + account.givenName
            }
            val serverAuth = account.serverAuthCode
            google_user = account.email

            LoginRepository().getAccessToken(serverAuth!!){accessToken ->
                if(accessToken == null){
                    Log.d("accessToken is null", "accessToken = $accessToken")
                    moveSignUpActivity()
                    return@getAccessToken
                }
                socialLogin(accessToken)
            }

            Log.d(
                TAG, "구글 로그인 사용자 정보 요청 성공" +
                        "\n회원아이디: ${google_user}" +
                        "\n서버 인증 코드: ${account.serverAuthCode}" +
                        "\n토큰: ${account.idToken}" +
                        "\n이메일: ${account.email}" +
                        "\n닉네임: ${userName} "
            )

        } catch (e: ApiException) {
            //Log.e(SignFragment::class.java.simpleName, e.stackTraceToString())
            Log.d(TAG, "에러 : $e")
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)
        setContentView(loginBinding.root)

        //Log.d(TAG, "keyhash ${Utility.getKeyHash(this)}")
        //DP1zQOa5mjnTte/hgypxWr7Llig=

        loginState = intent.getBooleanExtra("logoutState", true)

        if (loginState == false) {  //설정 화면에서 로그아웃 버튼을 누르면 false 값이 전달됨
            googleSignInClient.signOut().addOnCompleteListener {
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                Log.d(TAG, "로그아웃 후 닉네임: $userName")
            }
            googleSignInClient.revokeAccess().addOnCompleteListener {

            }
        }

        loginBinding.googleLogin.setOnClickListener {
            googleLogin()
        }
    }

    override fun onStart() {  // 로그인 된 기록이 있는 경우 바로 홈 화면으로 넘어감
        super.onStart()
        gsa = GoogleSignIn.getLastSignedInAccount(this)
        val account = gsa?.account

        if (gsa != null) {
            if (gsa!!.familyName == null) {
                userName = gsa!!.givenName
            } else if (gsa!!.givenName == null) {
                userName = gsa!!.familyName
            } else {
                userName = gsa!!.familyName + gsa!!.givenName
            }
            val serverAuth = gsa!!.serverAuthCode


            if (account != null) {
                relogin()
            }

            Log.d(TAG, "이미 로그인 됨 " + gsa?.email.toString() + "\n $serverAuth \n${gsa!!.idToken}")
        } else {
            Toast.makeText(this, "로그인 해야합니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
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
            //.requestProfile()
            .requestIdToken(google_client_id)
            .requestServerAuthCode(google_client_id, false) // string 파일에 저장해둔 client id 를 이용해 server authcode를 요청한다.
            .requestEmail() // 이메일도 요청
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun moveSignUpActivity() {
        val intent = Intent(applicationContext, BottomNavigationActivity::class.java)
        intent.putExtra("username", google_user)
        intent.putExtra(resources.getString(R.string.nickname_key), userName)
        startActivity(intent)
        finish()
    }

    private fun socialLogin(accessToken: String){
        callSocialLogin = UserRetrofit.getApiService()!!.socialLogin(SocialLoginRequest( accessToken))
        callSocialLogin.enqueue(object : Callback<SocialLoginResponse>{
            override fun onResponse(
                call: Call<SocialLoginResponse>,
                response: Response<SocialLoginResponse>
            ) {
                if(response.isSuccessful){
                    val result = response.body()
                    if(result != null){
                        val backendApiAccessToken = result.accessToken
                        val backendApiRefreshToken = result.refreshToken
                        val newUserCheck = result.newUser
                        Log.d("BackEnd API SocialLogin Success", "accessToken : $backendApiAccessToken\nrefreshToken : $backendApiRefreshToken\nnewUser : $newUserCheck")
                        saveToken(backendApiAccessToken, backendApiRefreshToken)
                        moveSignUpActivity()
                    }else{
                        Log.d("BackEnd API SocialLogin result is null", "val result : SocialLoginResponse? = response.body()")
                    }
                }
                else{
                    Log.d("BackEnd API SocialLogin response not successful", "Error : ${response.code()}")
                }
            }

            override fun onFailure(call: Call<SocialLoginResponse>, t: Throwable) {
                Log.d("BackEnd API SocialLogin Failure", "Fail : ${t.printStackTrace()}\n Error message : ${t.message}")
            }
        })
    }

    private fun saveToken(accessToken : String, refreshToken : String){
        lifecycleScope.launch {
            Log.d("saveToken in dataStore", "start")
            Log.d("saveToken", " context.dataStore = ${this@LoginActivity?.dataStore}")
            this@LoginActivity.dataStore.edit {
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

    private fun relogin() {
        lifecycleScope.launch {
            Log.d("onStart lifecycleScope.launch", "start")
            try {
                Log.d("DataStore", "this@LoginActivity.dataStore = ${this@LoginActivity.dataStore}")
                Log.d("DataStore", "this@LoginActivity.dataStore.data = ${this@LoginActivity.dataStore.data}")
                this@LoginActivity.dataStore.data.collect{ it ->
                    Log.d("onStart dataStore.data.collect", "start")
                    val accessToken = it[UserDataStore.ACCESS_TOKEN]
                    val refreshToken = it[UserDataStore.REFRESH_TOKEN]
                    if (accessToken != null && refreshToken != null) {
                        Log.d("BackEnd API AccessToken saved in DataStore", "accessToken is not null")
                        TokenManager.authorize(accessToken, refreshToken, resources.getString(R.string.prefix_of_access_token), resources.getString(R.string.prefix_of_refresh_token)) {authorizeCheck, accessToken, refreshToken->
                            if (authorizeCheck) {
                                Log.d("Authorize is success", "TokenManager.authorize is true")
                                if(accessToken != null && refreshToken != null){
                                    Log.d("Authorize accessToken&refreshToken is not null", "accessToken : $accessToken\nrefreshToken : $refreshToken")
                                    saveToken(accessToken, refreshToken)
                                    moveSignUpActivity()
                                }else{
                                    Toast.makeText(this@LoginActivity, "로그인을 해야 합니다.", Toast.LENGTH_SHORT).show()
                                }
                            }else{
                                Toast.makeText(this@LoginActivity, "로그인을 해야 합니다.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }else{
                        Toast.makeText(this@LoginActivity, "로그인을 해야 합니다.", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("DataStore Error", "DataStore operation failed: ${e.message}")
            }
            Log.d("onStart lifecycleScope.launch", "end")
        }
    }
}
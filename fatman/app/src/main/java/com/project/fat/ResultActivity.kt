package com.project.fat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.google.android.filament.IndirectLight
import com.project.fat.RunningTimeActivity.Companion.runningFinalData
import com.project.fat.data.dto.CreateHistoryResponse
import com.project.fat.dataStore.UserDataStoreKey
import com.project.fat.dataStore.UserDataStoreKey.USER_ID
import com.project.fat.dataStore.UserDataStoreKey.dataStore
import com.project.fat.databinding.ActivityResultBinding
import com.project.fat.retrofit.client.HistoryRetrofit
import com.project.fat.tokenManager.TokenManager
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.utils.colorOf
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var modelUrl : String? = null

    private lateinit var callCreateHistory: Call<CreateHistoryResponse>
    private lateinit var modelNode: ModelNode

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        modelUrl = intent.getStringExtra("glbFileLocation")


        modelNode = ModelNode(binding.monster3d.engine).apply {
            position = Position(x = 0.0f, y = 0.0f, z = -4.0f)
            rotation = Rotation(x = 0.0f, y = 2.5f, z = 0.0f)
        }

        lifecycleScope.launchWhenCreated {
            val modelInstance = modelUrl?.let {
                modelNode.loadModelGlb(
                    context = this@ResultActivity,
                    glbFileLocation = it
                )
            }
        }

        binding.goHome.setOnClickListener{
            sendNewHistory()
        }

        val background = colorOf(resources.getColor(R.color.translucent_white))

        binding.monster3d.addChild(modelNode)
        binding.monster3d.scene.skybox?.setColor(background[0], background[1], background[2], background[3])

        binding.distance.text =
            (runningFinalData?.distance + " km") ?: getString(R.string.data_miss)
        binding.time.text = (runningFinalData?.time + " time") ?: getString(R.string.data_miss)
    }

    private fun sendNewHistory(){
        //REST API createHistory
        lifecycleScope.launch {
            this@ResultActivity.dataStore.data.map {
                val todayMonsterNum = (it[UserDataStoreKey.TODAY_MONSTER_NUM] ?: 0) + 1
                val accessToken = TokenManager.getAccessToken()

                callCreateHistory = HistoryRetrofit.getApiService()!!.createHistory(accessToken.toString(), todayMonsterNum, runningFinalData!!.distance.toDouble(), runningFinalData!!.time)
                callCreateHistory.enqueue(object : Callback<CreateHistoryResponse>{
                    override fun onResponse(
                        call: Call<CreateHistoryResponse>,
                        response: Response<CreateHistoryResponse>
                    ) {
                        if(response.isSuccessful){
                            val result = response.body()
                            if(result != null){
                                saveTodayMonsterNum(todayMonsterNum)
                            }else{
                                Log.d("BackEnd API createHistory result is null", "val result : SocialLoginResponse? = response.body()")
                            }
                        }else{
                            Log.d("BackEnd API SocialLogin response not successful", "Error : ${response.code()}")
                        }
                    }

                    override fun onFailure(
                        call: Call<CreateHistoryResponse>,
                        t: Throwable
                    ) {
                        Log.d("BackEnd API SocialLogin Failure", "Fail : ${t.printStackTrace()}\n Error message : ${t.message}")
                        Toast.makeText(this@ResultActivity, "전송 오류 : 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
                    }

                })
            }
        }
    }

    private fun saveTodayMonsterNum(todayMonsterNum : Int){
        lifecycleScope.launch {
            applicationContext.dataStore.edit {
                Log.d("saveTodayMonsterNum", "start")
                it[UserDataStoreKey.TODAY_MONSTER_NUM] = todayMonsterNum
                Log.d("saveTodayMonsterNum", "end")
            }
            goHome()
        }
    }

    private fun goHome(){
        startActivity(Intent(this@ResultActivity, BottomNavigationActivity::class.java))
        finish()
    }
}
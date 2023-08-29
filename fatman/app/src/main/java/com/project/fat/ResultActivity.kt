package com.project.fat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.google.android.filament.IndirectLight
import com.project.fat.RunningTimeActivity.Companion.runningFinalData
import com.project.fat.data.dto.CreateHistoryResponse
import com.project.fat.databinding.ActivityResultBinding
import com.project.fat.retrofit.client.HistoryRetrofit
import io.github.sceneview.math.Position
import io.github.sceneview.math.Rotation
import io.github.sceneview.node.ModelNode
import io.github.sceneview.utils.colorOf
import retrofit2.Call

class ResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResultBinding
    private var modelUrl : String? = null

    private lateinit var callCreateFillEventHistory: Call<CreateHistoryResponse>
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
            startActivity(Intent(this, BottomNavigationActivity::class.java))
            finish()
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
    }
}
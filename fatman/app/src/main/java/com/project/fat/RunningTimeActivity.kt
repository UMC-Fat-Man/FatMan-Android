package com.project.fat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.project.fat.databinding.ActivityRunningTimeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import java.lang.Math.round
import kotlin.random.Random

class RunningTimeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRunningTimeBinding
    private var PLAY_BUTTON_PAUSE_STATE = false
    private lateinit var timeJob: Job
    private lateinit var distanceJob : Job
    private var time = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeCoroutine(time)
        distanceCoroutine()

        binding.playBtn.setOnClickListener {
            if(PLAY_BUTTON_PAUSE_STATE && timeJob.isCancelled){
                timeCoroutine(time)
                distanceCoroutine()
                PLAY_BUTTON_PAUSE_STATE = false
            }else if(!PLAY_BUTTON_PAUSE_STATE && !timeJob.isCancelled){
                timeJob.cancel()
                distanceJob.cancel()
                PLAY_BUTTON_PAUSE_STATE = true
            }else{
                Log.d("timeCoroutine", "play button is not correct or timejob is not cancelled")
                Toast.makeText(this, "스톱워치 문제 발생, 초기화합니다.", Toast.LENGTH_SHORT).show()
                PLAY_BUTTON_PAUSE_STATE = false
                timeJob.cancel()
                distanceJob.cancel()
                timeCoroutine(time)
                distanceCoroutine()
            }
        }
    }

    private fun timeCalculate(time : Int) : String? {
        var timeString : String? = null

        if(time >= 0){
            timeString = (time/60).toString() + ":" + if((time%60) < 10) {
                "0" + (time%60).toString()
            } else {
                (time%60).toString()
            }
        }else{
            Log.d("time", "time < 0")
        }

        return timeString
    }

    private fun timeCoroutine(t : Int) {
        timeJob = lifecycleScope.launchWhenCreated {
            var time = t
            while(true){
                val check = timeCalculate(time)
                if(check.isNullOrBlank())
                {
                    time = 0
                    this.cancel()
                }else{
                    binding.time.text = check
                    delay(1000)
                    time++
                }
            }
        }
    }

    private fun distanceCoroutine() {
        distanceJob = lifecycleScope.launchWhenCreated {
            while(true)
            {
                //read distance
                binding.kilometer.text = (round(Random.nextFloat()*1000) / 1000.0).toString()
                delay(500)
            }
        }
    }
}
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
    private var playButtonPauseState = false
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
            if(playButtonPauseState && timeJob.isCancelled){
                timeCoroutine(time)
                distanceCoroutine()
                playButtonPauseState = false
            }else if(!playButtonPauseState && !timeJob.isCancelled){
                timeJob.cancel()
                distanceJob.cancel()
                playButtonPauseState = true
            }else{
                Log.d("timeCoroutine", "play button is not correct or timejob is not cancelled")
                Toast.makeText(this, "스톱워치 문제 발생, 초기화합니다.", Toast.LENGTH_SHORT).show()
                playButtonPauseState = false
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
            var tm = t
            while(true){
                val check = timeCalculate(tm)
                if(check.isNullOrBlank())
                {
                    time = tm
                    this.cancel()
                }else{
                    binding.time.text = check
                    delay(1000)
                    tm++
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
package com.project.fat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.project.fat.data.runningData.ResultDistanceTime
import com.project.fat.databinding.ActivityRunningTimeBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import java.lang.Math.round
import kotlin.random.Random

class RunningTimeActivity : AppCompatActivity() {
    private lateinit var binding : ActivityRunningTimeBinding
    private lateinit var timeJob : Job
    private lateinit var distanceJob : Job
    private var time = 0

    private lateinit var fusedLocationClient : FusedLocationProviderClient
    private lateinit var locationCallback : LocationCallback

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRunningTimeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        timeCoroutine(time)
        distanceCoroutine()

        binding.imageView.setOnClickListener {
            saveRunningFinalData()
            //임시
            startActivity(Intent(this, ArActivity::class.java))
        }

        binding.pauseBtn.setOnClickListener {
            timeJob.cancel()
            distanceJob.cancel()
            binding.pauseBtn.visibility = View.GONE
            binding.startBtn.visibility = View.VISIBLE
            binding.stopBtn.visibility = View.VISIBLE
        }

        binding.startBtn.setOnClickListener{
            timeCoroutine(time)
            distanceCoroutine()
            binding.stopBtn.visibility = View.GONE
            binding.startBtn.visibility = View.GONE
            binding.pauseBtn.visibility = View.VISIBLE
        }

        binding.stopBtn.setOnClickListener {
            time = 0
            timeCoroutine(time)
            distanceCoroutine()
            binding.stopBtn.visibility = View.GONE
            binding.startBtn.visibility = View.GONE
            binding.pauseBtn.visibility = View.VISIBLE
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
        timeJob = lifecycleScope.launchWhenStarted {
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
                    time = tm
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

    private fun saveRunningFinalData() {
        runningFinalData = ResultDistanceTime(binding.time.text.toString(), binding.kilometer.text.toString())
    }

    companion object{
        var runningFinalData : ResultDistanceTime? = null
    }
}
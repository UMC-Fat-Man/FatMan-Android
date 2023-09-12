package com.project.fat.fragment.bottomNavigationActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.project.fat.LoadingActivity
import com.project.fat.R
import com.project.fat.data.dto.GetHistoryResponse
import com.project.fat.data.runningData.ResultDistanceTime
import com.project.fat.databinding.FragmentHomeBinding
import com.project.fat.retrofit.api_interface.HistoryService
import com.project.fat.retrofit.client.HistoryRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class HomeFragment : Fragment() {
    //프래그먼트는 액티비티보다 수명이 길기에 뷰바인딩 정보가 필요 이상으로 저장되어 있을 수 있습니다.
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var moveButton:ImageButton // 출동 버튼

    private lateinit var nicknameTextView: TextView

    private lateinit var monsterNumber:TextView



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val historyApiService : HistoryService = HistoryRetrofit.getApiService()!!


        historyApiService.getHistory().enqueue(object : Callback<GetHistoryResponse>{
            override fun onResponse(
                call: Call<GetHistoryResponse>,
                response: Response<GetHistoryResponse>
            ) {
                if(response.isSuccessful){
                    val r = response.body()
                    val mn = r!![0]?.monsterNum
                    monsterNumber=view.findViewById(R.id.monsterRecordText)
                    monsterNumber.text= mn.toString()
                }
            }

            override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {

            }

        })

        nicknameTextView = view.findViewById(R.id.nickname)


        var nickname = arguments?.getString("nickname")
        nicknameTextView.text = nickname

        moveButton = view.findViewById(R.id.move)

        moveButton.setOnClickListener {
            // 액티비티로 이동하는 코드
            val intent = Intent(activity, LoadingActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
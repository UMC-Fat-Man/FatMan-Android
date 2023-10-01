package com.project.fat.fragment.bottomNavigationActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.project.fat.LoadingActivity
import com.project.fat.R
import com.project.fat.data.dto.GetHistoryResponse
import com.project.fat.databinding.FragmentHomeBinding
import com.project.fat.retrofit.api_interface.HistoryService
import com.project.fat.retrofit.client.HistoryRetrofit
import com.project.fat.tokenManager.TokenManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var moveButton: ImageButton
    private lateinit var nicknameTextView: TextView
    private lateinit var moneyTextView: TextView
    private lateinit var monsterNumber: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val historyApiService: HistoryService = HistoryRetrofit.getApiService()!!

        nicknameTextView = view.findViewById(R.id.nickname)
        moneyTextView = view.findViewById(R.id.money)
        var nickname = arguments?.getString("nickname")
        var money = arguments?.getInt("money")
        nicknameTextView.text = nickname
        moneyTextView.text = money.toString()

        monsterNumber = view.findViewById(R.id.monsterRecordText)

        historyApiService.getHistory(accessToken = TokenManager.getAccessToken()!!).enqueue(object : Callback<GetHistoryResponse> {
            override fun onResponse(
                call: Call<GetHistoryResponse>,
                response: Response<GetHistoryResponse>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    val mn = responseData?.get(0)?.monsterNum
                    monsterNumber.text = mn.toString()
                } else {
                    Log.w("Retrofit", "Response Not Successful ${response.code()}")
                }
            }

            override fun onFailure(call: Call<GetHistoryResponse>, t: Throwable) {
                Log.e("Retrofit", "Error")
            }
        })

        moveButton = view.findViewById(R.id.move)
        moveButton.setOnClickListener {
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

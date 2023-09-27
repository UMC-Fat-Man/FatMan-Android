package com.project.fat.fragment.bottomNavigationActivity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fat.adapter.RecyclerviewAdapter

import com.project.fat.data.dto.WeekRankResponseModel

import com.project.fat.databinding.FragmentWeekRankingBinding
import com.project.fat.retrofit.client.RankObject
import com.project.fat.retrofit.api_interface.RankService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeekRankingFragment : Fragment() {
    lateinit var binding: FragmentWeekRankingBinding
    private var RankingApiService: RankService = RankObject.getApiService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeekRankingBinding.inflate(layoutInflater)

        //val list = getTopWeekRank(2023,7)
        val list = getWeekRank()

        binding.recyclerview.adapter = RecyclerviewAdapter(list)
        binding.recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
    fun getWeekRank(): ArrayList<WeekRankResponseModel>{
        lateinit var list: ArrayList<WeekRankResponseModel>
        RankingApiService.getWeekRank().enqueue(object : Callback<ArrayList<WeekRankResponseModel>>{
            override fun onResponse(
                call: Call<ArrayList<WeekRankResponseModel>>,
                response: Response<ArrayList<WeekRankResponseModel>>
            ) {
                if(response.isSuccessful){
                    list = response.body()!!
                    val id = response.body()!![1].id
                    val monsterNum = response.body()!![1].monsterNum
                    val user = response.body()!![1].user
                    val distance = response.body()!![1].distance
                    val yearNum = response.body()!![1].yearNum
                    val weekNum = response.body()!![1].weekNum
                    Log.d(
                        TAG, "Id: $id" +
                                "\nMonsterNum: $monsterNum" +
                                "\nDistance: $distance" +
                                "\nUser Name: ${user.name}" +
                                "\nYear: $yearNum" +
                                "\nWeek: $weekNum"
                    )
                }
                else
                    Log.d(TAG, response.code().toString())
            }

            override fun onFailure(call: Call<ArrayList<WeekRankResponseModel>>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
        return list
    }

    fun getTopWeekRank(year: Int, week: Int): ArrayList<WeekRankResponseModel>{
        lateinit var list: ArrayList<WeekRankResponseModel>
        RankingApiService.getTopWeekRank(year, week).enqueue(object : Callback<ArrayList<WeekRankResponseModel>>{
            override fun onResponse(
                call: Call<ArrayList<WeekRankResponseModel>>,
                response: Response<ArrayList<WeekRankResponseModel>>
            ) {
                if(response.isSuccessful){
                    list = response.body()!!
                    val id = response.body()!![1].id
                    val monsterNum = response.body()!![1].monsterNum
                    val user = response.body()!![1].user
                    val distance = response.body()!![1].distance
                    val yearNum = response.body()!![1].yearNum
                    val weekNum = response.body()!![1].weekNum
                    Log.d(
                        TAG, "Id: $id" +
                                "\nMonsterNum: $monsterNum" +
                                "\nDistance: $distance" +
                                "\nUser Name: ${user.name}" +
                                "\nYear: $yearNum" +
                                "\nWeek: $weekNum"
                    )
                }
            }

            override fun onFailure(call: Call<ArrayList<WeekRankResponseModel>>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
        return list
    }
}
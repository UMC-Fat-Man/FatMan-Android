package com.project.fat.fragment.bottomNavigationActivity

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fat.adapter.RecyclerviewAdapter2
import com.project.fat.data.dto.TotalRankResponseModel
import com.project.fat.databinding.FragmentTotalRankingBinding
import com.project.fat.retrofit.client.RankObject
import com.project.fat.retrofit.api_interface.RankService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TotalRankingFragment : Fragment() {
    lateinit var binding: FragmentTotalRankingBinding
    private var RankingApiService: RankService = RankObject.getApiService()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTotalRankingBinding.inflate(layoutInflater)

        val list = getTopTotalRank()
        binding.recyclerview.adapter = RecyclerviewAdapter2(list)
        binding.recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return binding.root
    }
    fun totalRank() : TotalRankResponseModel{
        lateinit var list: TotalRankResponseModel
        RankingApiService!!.totalRank().enqueue(object : Callback<TotalRankResponseModel> {
            override fun onResponse (
                call: Call<TotalRankResponseModel>,
                response: Response<TotalRankResponseModel>
            ) {
                if (response.isSuccessful) {
                    list = response.body()!!
                    val id = response.body()!![1].id
                    val monsterNum = response.body()!![1].monsterNum
                    val user = response.body()!![1].user
                    val distance = response.body()!![1].distance
                    Log.d(
                        TAG, "Id: $id" +
                                "\nMonsterNum: $monsterNum" +
                                "\nDistance: $distance" +
                                "\nlist: $list"
                    )

                }
            }

            override fun onFailure(call: Call<TotalRankResponseModel>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
        return list
    }
    fun getTopTotalRank(): TotalRankResponseModel{
        lateinit var list: TotalRankResponseModel
        RankingApiService.getTopTotalRank().enqueue(object : Callback<TotalRankResponseModel>{
            override fun onResponse(
                call: Call<TotalRankResponseModel>,
                response: Response<TotalRankResponseModel>
            ) {
                if(response.isSuccessful){
                    list = response.body()!!
                    val id = response.body()!![1].id
                    val monsterNum = response.body()!![1].monsterNum
                    val user = response.body()!![1].user
                    val distance = response.body()!![1].distance
                    Log.d(
                        TAG, "Id: $id" +
                                "\nMonsterNum: $monsterNum" +
                                "\nDistance: $distance" +
                                "\nUser Name: ${user.name}"
                    )
                }
            }

            override fun onFailure(call: Call<TotalRankResponseModel>, t: Throwable) {
                Log.e(TAG, "getOnFailure: ",t.fillInStackTrace() )
            }

        })
        return list
    }
}
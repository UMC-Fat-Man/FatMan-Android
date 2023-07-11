package com.project.fat.fragment.bottomNavigationActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fat.R
import com.project.fat.databinding.FragmentRankingBinding
import com.project.fat.adapter.RecyclerviewAdapter

class rankingFragment : Fragment() {
    private lateinit var rankingBinding: FragmentRankingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ranking, container, false)


        rankingBinding = FragmentRankingBinding.inflate(layoutInflater)

        rankingBinding.btn1.setOnClickListener {
            rankingBinding.btn1.setBackgroundResource(R.drawable.dark_blue_round_view_with_shadow)
            rankingBinding.btn2.setBackgroundResource(R.drawable.light_blue_round_view_with_shadow)
        }
        rankingBinding.btn2.setOnClickListener {
            rankingBinding.btn1.setBackgroundResource(R.drawable.light_blue_round_view_with_shadow)
            rankingBinding.btn2.setBackgroundResource(R.drawable.dark_blue_round_view_with_shadow)
        }

        rankingBinding.recyclerview.adapter = RecyclerviewAdapter()
        rankingBinding.recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        var listviewAdapter = listviewAdapter(this)
//        rankingBinding.recyclerview.adapter = listviewAdapter
        return rankingBinding.root


    }
}
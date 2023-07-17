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
import com.project.fat.databinding.FragmentCalendarBinding

class RankingFragment : Fragment() {
    private var _binding: FragmentRankingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_ranking, container, false)
        _binding = FragmentRankingBinding.inflate(inflater, container, false)

        with(binding){
            btn1.setOnClickListener {
                btn1.setBackgroundResource(R.drawable.dark_blue_round_view_with_shadow)
                btn2.setBackgroundResource(R.drawable.light_blue_round_view_with_shadow)
            }
            btn2.setOnClickListener {
                btn1.setBackgroundResource(R.drawable.light_blue_round_view_with_shadow)
                btn2.setBackgroundResource(R.drawable.dark_blue_round_view_with_shadow)
            }
            recyclerview.adapter = RecyclerviewAdapter()
            recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }

//        var listviewAdapter = listviewAdapter(this)
//        rankingBinding.recyclerview.adapter = listviewAdapter
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
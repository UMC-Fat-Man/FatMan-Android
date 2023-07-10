package com.project.fat

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.project.fat.databinding.FragmentRankingBinding


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [rankingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class rankingFragment : Fragment() {
    private lateinit var rankingBinding: FragmentRankingBinding
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

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

        rankingBinding.recyclerview.adapter = recyclerviewAdapter()
        rankingBinding.recyclerview.layoutManager =LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

//        var listviewAdapter = listviewAdapter(this)
//        rankingBinding.recyclerview.adapter = listviewAdapter
        return rankingBinding.root


    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment rankingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            rankingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
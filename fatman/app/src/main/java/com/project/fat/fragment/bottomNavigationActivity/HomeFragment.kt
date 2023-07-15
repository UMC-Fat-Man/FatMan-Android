package com.project.fat.fragment.bottomNavigationActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.project.fat.CountActivity
import com.project.fat.R
import com.project.fat.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    //프래그먼트는 액티비티보다 수명이 길기에 뷰바인딩 정보가 필요 이상으로 저장되어 있을 수 있습니다.
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var moveButton:ImageButton // 출동 버튼


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        moveButton = view.findViewById(R.id.move)

        moveButton.setOnClickListener {
            // 액티비티로 이동하는 코드
            val intent = Intent(activity, CountActivity::class.java)
            startActivity(intent)
        }
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
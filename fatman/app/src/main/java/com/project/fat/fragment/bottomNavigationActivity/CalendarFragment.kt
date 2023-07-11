package com.project.fat.fragment.bottomNavigationActivity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.project.fat.R
import com.project.fat.databinding.FragmentCalendarBinding

class CalendarFragment : Fragment() {
    //프래그먼트는 액티비티보다 수명이 길기에 뷰바인딩 정보가 필요 이상으로 저장되어 있을 수 있습니다.
    private var _binding : FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
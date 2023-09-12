package com.project.fat.fragment.bottomNavigationActivity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.project.fat.LoadingActivity
import com.project.fat.R
import com.project.fat.data.store.StoreAvata
import com.project.fat.dataStore.UserDataStore
import com.project.fat.dataStore.selectedFatmanInterface.OnSelectedFatmanListener
import com.project.fat.databinding.FragmentHomeBinding
import com.project.fat.databinding.FragmentStoreBinding
import com.project.fat.selectedFatmanManager.SelectedFatmanManager
import kotlinx.coroutines.launch

class HomeFragment : Fragment(){
    //프래그먼트는 액티비티보다 수명이 길기에 뷰바인딩 정보가 필요 이상으로 저장되어 있을 수 있습니다.
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.nickname.text = arguments?.getString(resources.getString(R.string.nickname_key)) ?: resources.getString(R.string.unknown_player)
        binding.move.setOnClickListener {
            // 액티비티로 이동하는 코드
            val intent = Intent(activity, LoadingActivity::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            SelectedFatmanManager.initSelectedFatmanManager(requireContext())
            Glide.with(binding.root)
                .load(SelectedFatmanManager.getSelectedFatmanImageUrl())
                .into(binding.fatman)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
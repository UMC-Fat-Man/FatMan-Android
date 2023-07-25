package com.project.fat.fragment.bottomNavigationActivity

import StorePagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.project.fat.R
import com.project.fat.data.store.StoreAvata
import com.project.fat.databinding.FragmentStoreBinding
import kotlinx.coroutines.launch

class StoreFragment : Fragment() {
    private var _binding : FragmentStoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val avataData = getListOfStoreAvata()
        val storeAdapter = StorePagerAdapter(avataData)
        binding.store.adapter = storeAdapter

        TabLayoutMediator(binding.indicator, binding.store, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if((avataData[position].activated) && !(tab.isSelected))
                tab.view.setBackgroundResource(R.drawable.activated_tab_pager_indicator)
        }).attach()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getListOfStoreAvata() : List<StoreAvata> {
        var storeAvataDatas = mutableListOf<StoreAvata>()

        //나중에 RESTAPI
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            true,
            "test1"))
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            true,
            "test2"))
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            false,
            "test3"))
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            false,
            "test4"))

        return storeAvataDatas
    }
}
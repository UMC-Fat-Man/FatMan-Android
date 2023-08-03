package com.project.fat.fragment.bottomNavigationActivity

import StorePagerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.project.fat.R
import com.project.fat.data.store.StoreAvata
import com.project.fat.dataStore.UserDataStoreKey
import com.project.fat.dataStore.UserDataStoreKey.USER_ID
import com.project.fat.dataStore.UserDataStoreKey.dataStore
import com.project.fat.databinding.FragmentStoreBinding
import com.project.fat.databinding.StoreViewBinding
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.waitMillis
import kotlin.concurrent.thread

class StoreFragment : Fragment(), StorePagerAdapter.OnSelectButtonClickListener {
    private var _binding : FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private var selectedFatMan : StoreAvata? = null

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
        val storeAdapter = StorePagerAdapter(avataData, this)
        binding.store.adapter = storeAdapter

        TabLayoutMediator(binding.indicator, binding.store, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if((avataData[position].activated) && !(tab.isSelected))
                tab.view.setBackgroundResource(R.drawable.activated_tab_pager_indicator)
        }).attach()
    }

    override fun onStop() {
        super.onStop()
        if(selectedFatMan != null)
        saveSelectedFatManImage(selectedFatMan!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getListOfStoreAvata() : MutableList<StoreAvata> {
        var storeAvataDatas = mutableListOf<StoreAvata>()

        //나중에 RESTAPI
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            true,
            "test1", true))
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            true,
            "test2", false))
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            false,
            "test3", false))
        storeAvataDatas.add(StoreAvata("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            false,
            "test4", false))

        return storeAvataDatas
    }

    fun saveSelectedFatManImage(data : StoreAvata){
        lifecycleScope.launch {
            context?.dataStore?.edit {
                it[UserDataStoreKey.SELECTED_FATMAN_IMAGE] = data.fatmanImage
            }
        }

        //RESTAPI
    }

    override fun onSelectButtonClick(
        binding: StoreViewBinding,
        data: MutableList<StoreAvata>,
        position: Int
    ) {
        if(data[position].selected){
            binding.select.setImageResource(R.drawable.default_store_avata)
            data[position].selected = false
        }else{
            binding.select.setImageResource(R.drawable.selected_store_avata)
            for(i in data){
                i.selected = false
            }
            data[position].selected = true
            selectedFatMan = data[position]
        }
    }
}
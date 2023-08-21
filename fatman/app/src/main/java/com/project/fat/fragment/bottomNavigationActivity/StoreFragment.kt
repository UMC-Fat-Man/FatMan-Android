package com.project.fat.fragment.bottomNavigationActivity

import StorePagerAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.project.fat.R
import com.project.fat.data.dto.Fatman
import com.project.fat.data.store.StoreAvata
import com.project.fat.dataStore.UserDataStoreKey
import com.project.fat.dataStore.UserDataStoreKey.dataStore
import com.project.fat.databinding.FragmentStoreBinding
import com.project.fat.databinding.StoreViewBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import kotlin.concurrent.thread

class StoreFragment : Fragment(), StorePagerAdapter.OnSelectButtonClickListener {
    private var _binding : FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private var selectedFatMan : StoreAvata? = null
    private lateinit var storeAdapter : StorePagerAdapter

    private lateinit var callFatman : Call<Fatman>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("lifecycle", "onCreateView")
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("lifecycle", "onViewCreated")
        super.onViewCreated(view, savedInstanceState)

        val avataData = getListOfStoreAvata()
        storeAdapter = StorePagerAdapter(avataData, this)

        binding.store.adapter = storeAdapter
        TabLayoutMediator(binding.indicator, binding.store, TabLayoutMediator.TabConfigurationStrategy { _, _ ->
        }).attach()
    }

    override fun onPause() {
        if(selectedFatMan != null)
            saveSelectedFatMan(selectedFatMan!!)
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getListOfStoreAvata() : MutableList<StoreAvata> {
        var storeAvataDatas = mutableListOf<StoreAvata>()

        //retrofit으로 아바타 리스트 받아오기

        Log.d("saveSelectedFatman in dataStore", " context.dataStore = ${context?.dataStore}")
        Log.d("saveSelectedFatman in dataStore", " context.dataStore.data = ${context?.dataStore?.data}")
        context?.dataStore?.data?.map {
            val selectedFatmanId = it[UserDataStoreKey.SELECTED_FATMAN_ID] ?: 1
            val selectedFatmanImage = it[UserDataStoreKey.SELECTED_FATMAN_IMAGE] ?: "나중에 기본 이미지 주소 넣기"

            Log.d("getSelectedFatman in dataStore", "selectedFatmanId = $selectedFatmanId")
            Log.d("getSelectedFatman in dataStore", "selectedFatmanId = $selectedFatmanImage")

            //선택한 아바타와 대조하여 초기 세팅
        }

        //임시
        storeAvataDatas.add(StoreAvata(1, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            true,
            "test1", true))
        storeAvataDatas.add(StoreAvata(2, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            true,
            "test2", false))
        storeAvataDatas.add(StoreAvata(3, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            false,
            "test3", false))
        storeAvataDatas.add(StoreAvata(4, "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQVobgDD4FPvmwkHCqTbzxpAZCVrDqlYzEIVIUNOUDXZg&s",
            false,
            "test4", false))

        return storeAvataDatas
    }

    fun saveSelectedFatMan(data : StoreAvata){
        lifecycleScope.launch {
            Log.d("saveSelectedFatman in dataStore", "start")
            Log.d("saveSelectedFatman in dataStore", " context.dataStore = ${context?.dataStore}")
            context?.dataStore?.edit {
                it[UserDataStoreKey.SELECTED_FATMAN_IMAGE] = data.fatmanImage
                it[UserDataStoreKey.SELECTED_FATMAN_ID] = data.id
            }

            Log.d("saveSelectedFatman in dataStore", "end")
        }

        //RESTAPI
    }

    override fun onSelectButtonClick(
        binding: StoreViewBinding,
        data: MutableList<StoreAvata>,
        position: Int
    ) {
        if(data[position].selected){
            data[position].selected = false
            storeAdapter.notifyDataSetChanged()
        }else{
            for(i in data){
                i.selected = false
            }
            data[position].selected = true
            selectedFatMan = data[position]
            storeAdapter.notifyDataSetChanged()
            for(i in data){
                Log.d("select button", "id : ${i.id}, selected : ${i.selected}")
            }
        }
    }
}
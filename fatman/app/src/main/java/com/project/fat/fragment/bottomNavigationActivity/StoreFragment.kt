package com.project.fat.fragment.bottomNavigationActivity

import StorePagerAdapter
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.preferences.core.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.material.tabs.TabLayoutMediator
import com.project.fat.R
import com.project.fat.data.dto.Fatman
import com.project.fat.data.dto.UserFatman
import com.project.fat.data.store.StoreAvata
import com.project.fat.dataStore.UserDataStoreKey
import com.project.fat.dataStore.UserDataStoreKey.dataStore
import com.project.fat.databinding.FragmentStoreBinding
import com.project.fat.databinding.StoreViewBinding
import com.project.fat.retrofit.client.FatmanRetrofit
import com.project.fat.retrofit.client.UserFatmanRetrofit
import com.project.fat.tokenManager.TokenManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoreFragment : Fragment(), StorePagerAdapter.OnSelectButtonClickListener {
    private var _binding : FragmentStoreBinding? = null
    private val binding get() = _binding!!
    private var selectedFatMan : StoreAvata? = null
    private lateinit var context : Context
    private lateinit var storeAdapter : StorePagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        context = requireContext()

        val avataData = mutableListOf<StoreAvata>()

        storeAdapter = StorePagerAdapter(avataData, this@StoreFragment)
        binding.store.adapter = storeAdapter

        lifecycleScope.launch {
            Log.d("onViewCreated", "avataData.addAll(getListOfStoreAvata()) start")
            avataData.addAll(getListOfStoreAvata())
            Log.d("onViewCreated", "avataData.addAll(getListOfStoreAvata()) end")
            storeAdapter.notifyDataSetChanged()
            Log.d("storeAdapter", "${storeAdapter.itemCount}")

            TabLayoutMediator(binding.indicator, binding.store, TabLayoutMediator.TabConfigurationStrategy { _, _ ->
            }).attach()
            Log.d("onViewCreated", " lifecycleScope.launch end")
        }
    }


    override fun onPause() {
        if(selectedFatMan != null)
            saveSelectedFatMan(selectedFatMan!!)
        else{

        }
        super.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private suspend fun getListOfStoreAvata() : MutableList<StoreAvata> = suspendCoroutine {continuation ->
        lifecycleScope.launch {
            var storeAvataList = mutableListOf<StoreAvata>()
            val fatmanList = getFatman()
            val userFatmanList = getUserFatman()
            val selectedFatmanId = getSelectedFatmanId()
            for(i in 0..fatmanList.lastIndex){
                val id = fatmanList[i].fatmanId // fatmanID가 null일 경우 id도 null이 될 수 있도록 변경
                storeAvataList.add(StoreAvata(
                    id,
                    fatmanList[i].fatmanImageUrl ?: "", // fatmanImageURL이 null일 경우 빈 문자열로 처리
                    userFatmanList.fatmanId.any { it==id },
                    fatmanList[i].name ?: "", // name이 null일 경우 빈 문자열로 처리
                    id == selectedFatmanId
                ))

                Log.d("getListOfStoreAvata storeAvataList.add", "storeAvata[$i] : " +
                        "\n\tid=${storeAvataList[i].id}" +
                        "\n\tfatmanImage=${storeAvataList[i].fatmanImage}" +
                        "\n\tachieved=${storeAvataList[i].achieved}" +
                        "\n\tfatmanName=${storeAvataList[i].fatmanName}" +
                        "\n\tselected=${storeAvataList[i].selected}")
            }

            continuation.resume(storeAvataList)
        }
    }

    private suspend fun getFatman() : Fatman = suspendCoroutine { continuation ->
        FatmanRetrofit.getApiService()!!.getFatmanList()
            .enqueue(object : Callback<Fatman>{
                override fun onResponse(call: Call<Fatman>, response: Response<Fatman>) {
                    if(response.isSuccessful){
                        val result = response.body()
                        if(result != null){
                            continuation.resume(result)
                        }else{
                            Log.d("getFatman result is null", "val result = response.body()")
                            continuation.resumeWithException(NullPointerException("Result is null"))
                        }
                    }else{
                        Log.d("getFatman response is not success", "Error : ${response.code()}")
                        continuation.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: Call<Fatman>, t: Throwable) {
                    Log.d("getUserFatman Failure", "Fail : ${t.printStackTrace()}\n Error message : ${t.message}")
                    continuation.resumeWithException(t)
                }

            })
    }

    private suspend fun getUserFatman(): UserFatman = suspendCoroutine { continuation ->
        UserFatmanRetrofit.getApiService()!!
            .getUserFatman(resources.getString(R.string.prefix_of_access_token) + TokenManager.getAccessToken())
            .enqueue(object : Callback<UserFatman> {
                override fun onResponse(call: Call<UserFatman>, response: Response<UserFatman>) {
                    if (response.isSuccessful) {
                        val result = response.body()
                        if (result != null) {
                            continuation.resume(result)
                        } else {
                            Log.d("getUserFatman result is null", "val result = response.body()")
                            continuation.resumeWithException(NullPointerException("Result is null"))
                        }
                    } else {
                        Log.d("getUserFatman response is not success", "Error : ${response.code()}")
                        continuation.resumeWithException(HttpException(response))
                    }
                }

                override fun onFailure(call: Call<UserFatman>, t: Throwable) {
                    Log.d("getUserFatman Failure", "Fail : ${t.printStackTrace()}\n Error message : ${t.message}")
                    continuation.resumeWithException(t)
                }
            })
    }


    private suspend fun getSelectedFatmanId() : Long = suspendCoroutine { continuation ->
        lifecycleScope.launch {
            context.dataStore.data.collect {
                val seletedFatmanId = it[UserDataStoreKey.SELECTED_FATMAN_ID] ?:1
                Log.d("getSelectedFatman", "selectedFatmanId : $seletedFatmanId")
                continuation.resume(seletedFatmanId)
            }
        }
    }

    private fun saveSelectedFatMan(data : StoreAvata){
        lifecycleScope.launch {
            Log.d("saveSelectedFatman in dataStore", "start")
            Log.d("saveSelectedFatman in dataStore", " context.dataStore = ${context.dataStore}")
            context.dataStore.edit {
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
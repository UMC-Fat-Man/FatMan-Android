package com.project.fat

import android.content.ContentValues.TAG
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.service.autofill.UserData
import android.util.Log
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.fat.data.permission.Permission
import com.project.fat.data.store.StoreAvata
import com.project.fat.dataStore.UserDataStore
import com.project.fat.dataStore.selectedFatmanInterface.OnSelectedFatmanListener
import com.project.fat.databinding.ActivityBottomNavigationBinding
import com.project.fat.fragment.bottomNavigationActivity.CalFatFragment
import com.project.fat.fragment.bottomNavigationActivity.CalendarFragment
import com.project.fat.fragment.bottomNavigationActivity.HomeFragment
import com.project.fat.fragment.bottomNavigationActivity.RankingFragment
import com.project.fat.fragment.bottomNavigationActivity.SettingsFragment
import com.project.fat.fragment.bottomNavigationActivity.StoreFragment
import kotlinx.coroutines.launch

class BottomNavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener,
    OnSelectedFatmanListener {
    lateinit var binding: ActivityBottomNavigationBinding
    var nickname: String? = null
    var homeFragment = HomeFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        nickname = intent.getStringExtra("nickname")


        var bundle = Bundle()
        bundle.putString("nickname", nickname)
        homeFragment.arguments = bundle

        supportFragmentManager.beginTransaction().add(R.id.fragment_container_view,homeFragment).commitAllowingStateLoss()
        binding.bottomNavigation.selectedItemId = R.id.page_home
        //리스너 연결

        binding.bottomNavigation.setOnItemSelectedListener(this)


        ActivityCompat.requestPermissions(this, Permission.PERMISSIONS, Permission.PERMISSION_FLAG)
    }
    //바텀네비게이션 탭 선택에 따라 페이지 변경
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.page_store -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, StoreFragment())
                    .commitAllowingStateLoss()
                return true
            }

            R.id.page_ranking -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, RankingFragment())
                    .commitAllowingStateLoss()
                return true
            }

            R.id.page_home -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, homeFragment).commitAllowingStateLoss()
                return true
            }

            R.id.page_calendar -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, CalFatFragment())
                    .commitAllowingStateLoss()
                return true
            }

            R.id.page_setting -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_view, SettingsFragment())
                    .commitAllowingStateLoss()
                return true
            }

            else -> return false
        }
    }

    override fun onSaveSelectedFatman(selectedFatman: StoreAvata) {
        lifecycleScope.launch {
            UserDataStore.saveSelectedFatman(this@BottomNavigationActivity, selectedFatman)
        }
    }
}



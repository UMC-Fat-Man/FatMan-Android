package com.project.fat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.fat.databinding.ActivityBottomNavigationBinding
import com.project.fat.fragment.bottomNavigationActivity.CalendarFragment
import com.project.fat.fragment.bottomNavigationActivity.HomeFragment
import com.project.fat.fragment.bottomNavigationActivity.RankingFragment
import com.project.fat.fragment.bottomNavigationActivity.SettingsFragment
import com.project.fat.fragment.bottomNavigationActivity.StoreFragment

class BottomNavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityBottomNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //리스너 연결
        binding.bottomNavigation.setOnItemSelectedListener(this)
        binding.bottomNavigation.selectedItemId = R.id.page_home

        //viewpager 스와이프 가능여부
        binding.viewPager.isUserInputEnabled = false
    }
    //바텀네비게이션 탭 선택에 따라 페이지 변경
    override fun onNavigationItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            R.id.page_store -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view , StoreFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.page_ranking -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view , RankingFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.page_home -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view , HomeFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.page_calendar -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view , CalendarFragment()).commitAllowingStateLoss()
                return true
            }
            R.id.page_setting -> {
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container_view , SettingsFragment()).commitAllowingStateLoss()
                return true
            }
            else -> return false
        }
    }

}



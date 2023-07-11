package com.project.fat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2

import com.google.android.material.bottomnavigation.BottomNavigationView
import com.project.fat.adapter.ViewPagerAdapter
import com.project.fat.databinding.ActivityBottomNavigationBinding

class BottomNavigationActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var binding: ActivityBottomNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // viewPager에 어댑터 연결
        binding.viewPager.adapter = ViewPagerAdapter(this)
        // swipe를  통해 페이지 변경된 상태를 바텀네비게이션에도 적용
        binding.viewPager.registerOnPageChangeCallback(
            object : ViewPager2.OnPageChangeCallback(){

                override fun onPageSelected(position: Int){
                    super.onPageSelected(position)
                    binding.bottomNavigation.menu.getItem(position).isChecked = true

                }
            }


        )
        //리스너 연결
        binding.bottomNavigation.setOnItemSelectedListener(this)
    }
    //바텀네비게이션 탭 선택에 따라 페이지 변경
    override fun onNavigationItemSelected(item: MenuItem): Boolean{
        when(item.itemId){
            R.id.page_store -> {
                binding.viewPager.currentItem = 0
                return true
            }
            R.id.page_ranking -> {
                binding.viewPager.currentItem = 1
                return true
            }
            R.id.page_home -> {
                binding.viewPager.currentItem = 2
                return true
            }
            R.id.page_calendar -> {
                binding.viewPager.currentItem = 3
                return true
            }
            R.id.page_setting -> {
                binding.viewPager.currentItem = 4
                return true
            }
            else -> return false
        }
    }

}



package com.project.fat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.fat.fragment.bottomNavigationActivity.CalendarFragment
import com.project.fat.fragment.bottomNavigationActivity.FatbookFragment
import com.project.fat.fragment.bottomNavigationActivity.HomeFragment
import com.project.fat.fragment.bottomNavigationActivity.RankingFragment
import com.project.fat.fragment.bottomNavigationActivity.SettingsFragment
import com.project.fat.fragment.bottomNavigationActivity.StoreFragment


class ViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> StoreFragment()
            1 -> RankingFragment()
            2 -> HomeFragment()
            //3 -> CalendarFragment()
            3 -> FatbookFragment()
            else -> SettingsFragment()
        }
    }

}
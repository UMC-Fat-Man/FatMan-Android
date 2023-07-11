package com.project.fat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.project.fat.fragment.bottomNavigationActivity.calendarFragment
import com.project.fat.fragment.bottomNavigationActivity.homeFragment
import com.project.fat.fragment.bottomNavigationActivity.rankingFragment
import com.project.fat.fragment.bottomNavigationActivity.settingsFragment
import com.project.fat.fragment.bottomNavigationActivity.storeFragment


class ViewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> storeFragment()
            1 -> rankingFragment()
            2 -> homeFragment()
            3 -> calendarFragment()
            else -> settingsFragment()
        }
    }

}
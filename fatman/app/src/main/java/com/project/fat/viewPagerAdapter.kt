package com.project.fat

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class viewPagerAdapter(fragment: FragmentActivity): FragmentStateAdapter(fragment) {
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
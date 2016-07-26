package com.beyebe.fastdevframe.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by daiquanyi on 16/7/13.
 */
class DialViewPagerAdapter(private val fragments:Array<Fragment>, fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    init {
        // Clean fragments (only if the app is recreated (When user disable permission))
        if (fragmentManager.backStackEntryCount > 0) {
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }

        // Remove previous fragments (case of the app was restarted after changed permission on android 6 and higher)
        val fragmentList = fragmentManager.fragments
        if (fragmentList != null) {
            for (fragment in fragmentList) {
                if (fragment != null) {
                    fragmentManager.beginTransaction().remove(fragment).commit()
                }
            }
        }
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }
}

package com.beyebe.fastdevframe.adapter

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup
import com.beyebe.fastdevframe.fragment.DialFragment

import com.beyebe.fastdevframe.fragment.ShopFragment

import java.util.ArrayList

/**
 * Created by daiquanyi on 16/7/13.
 */
class DialViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    val fragments = ArrayList<Fragment>()
    var currentFragment: Fragment? = null

    init {
        fragments.add(DialFragment())
        fragments.add(ShopFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as Fragment
        }
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return fragments.size
    }
}

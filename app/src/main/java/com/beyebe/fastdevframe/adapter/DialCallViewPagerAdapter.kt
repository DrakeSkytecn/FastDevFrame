package com.beyebe.fastdevframe.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.ViewGroup

import com.beyebe.fastdevframe.fragment.DialCallFragment
import com.beyebe.fastdevframe.fragment.DialContactFragment

import java.util.ArrayList

/**
 * Created by daiquanyi on 16/7/13.
 */
class DialCallViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    var currentFragment: Fragment? = null
    private val fragments = ArrayList<Fragment>()

    init {
        fragments.add(DialCallFragment())
        fragments.add(DialContactFragment())
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun setPrimaryItem(container: ViewGroup?, position: Int, `object`: Any) {
        if (currentFragment !== `object`) {
            currentFragment = `object` as Fragment
        }
        super.setPrimaryItem(container, position, `object`)
    }
}

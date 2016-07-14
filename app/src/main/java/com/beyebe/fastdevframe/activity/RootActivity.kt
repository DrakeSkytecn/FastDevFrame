package com.beyebe.fastdevframe.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.adapter.DialViewPagerAdapter
import com.beyebe.fastdevframe.fragment.DialFragment
import kotlinx.android.synthetic.main.activity_root.*

class RootActivity : AppCompatActivity() {

    private val TAG: String = "RootActivity"

    private var navigationAdapter: AHBottomNavigationAdapter? = null
    private var dialViewPagerAdapter: DialViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        dialViewPagerAdapter = DialViewPagerAdapter(supportFragmentManager)
        root_view_pager.adapter = dialViewPagerAdapter
        val tabColors = applicationContext.resources.getIntArray(R.array.tab_colors)
        navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu)
        navigationAdapter!!.setupWithBottomNavigation(bottom_navigation, tabColors)
        bottom_navigation.setOnTabSelectedListener { position, isSelected ->
            Log.d(TAG, "" + isSelected)
            when (position) {
                0 -> {
                    if (isSelected) {
                        (dialViewPagerAdapter!!.fragments[0] as DialFragment).changeDialState()
                    }
                }
                else -> {

                }
            }
            root_view_pager.setCurrentItem(position, false)
            return@setOnTabSelectedListener true
        }
    }


}

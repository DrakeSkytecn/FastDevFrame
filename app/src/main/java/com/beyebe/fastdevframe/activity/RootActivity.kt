package com.beyebe.fastdevframe.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.anthonycr.grant.PermissionsManager
import com.anthonycr.grant.PermissionsResultAction
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.adapter.DialViewPagerAdapter
import com.beyebe.fastdevframe.fragment.DialFragment
import com.beyebe.fastdevframe.fragment.ShopFragment
import kotlinx.android.synthetic.main.activity_root.*

class RootActivity : AppCompatActivity() {

    private val TAG: String = "RootActivity"

    private var navigationAdapter: AHBottomNavigationAdapter? = null
    private var dialViewPagerAdapter: DialViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_root)
        PermissionsManager.getInstance().requestAllManifestPermissionsIfNecessary(this,
                object : PermissionsResultAction() {
                    override fun onGranted() {
                        // Proceed with initialization
                    }

                    override fun onDenied(permission: String) {
                        // Notify the user that you need all of the permissions
                    }
                })
        val fragments = arrayOf(DialFragment(), ShopFragment())
        dialViewPagerAdapter = DialViewPagerAdapter(fragments, supportFragmentManager)
        root_view_pager.adapter = dialViewPagerAdapter
        val tabColors = applicationContext.resources.getIntArray(R.array.tab_colors)
        navigationAdapter = AHBottomNavigationAdapter(this, R.menu.bottom_navigation_menu)
        navigationAdapter!!.setupWithBottomNavigation(bottom_navigation, tabColors)
        bottom_navigation.isColored = true
        bottom_navigation.setOnTabSelectedListener { position, isSelected ->
            Log.d(TAG, "" + isSelected)
            when (position) {
                0 -> {
                    val item = navigationAdapter!!.getMenuItem(0)
                    item.setIcon(R.mipmap.dial_down)
                    item.setTitle(R.string.menu_1_1)
                    if (isSelected) {
                        (fragments[0] as DialFragment).changeDialState(item)
                    }
                }
                else -> {
                    val item = navigationAdapter!!.getMenuItem(0)
                    item.setIcon(R.mipmap.dial_tab)
                    item.setTitle(R.string.menu_1_3)
                }
            }
            root_view_pager.setCurrentItem(position, false)
            return@setOnTabSelectedListener true
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.d(TAG, "RootActivity onRequestPermissionsResult")
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults)
    }

}

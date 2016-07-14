package com.beyebe.fastdevframe.activity

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log.d
import android.util.TypedValue
import android.view.Menu
import android.widget.AdapterView
import android.widget.SimpleAdapter
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.fragment.ContactFragment
import com.beyebe.fastdevframe.fragment.HomeFragment
import com.beyebe.fastdevframe.model.User
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), ContactFragment.OnContactFragmentInteractionListener {

    private var menuData: MutableList<HashMap<String, Any>> = mutableListOf()
    private val menuTitles = arrayOf("扫二维码", "个性装扮", "一键分享", "清除缓存")
    private val menuIcons = arrayOf(R.mipmap.xingxing_yellow, R.mipmap.ic_menu_black, R.mipmap.xingxing_yellow, R.mipmap.ic_menu_black)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initData()
        addListener()
    }

    private fun initView() {
        setSupportActionBar(toolbar)
        viewPager.adapter = PagerAdapter(supportFragmentManager)
        pagerSlidingTabStrip.setViewPager(viewPager)
        pagerSlidingTabStrip.shouldExpand = true
        pagerSlidingTabStrip.indicatorColor = Color.BLUE
        pagerSlidingTabStrip.dividerColor = Color.GRAY
        pagerSlidingTabStrip.background = ColorDrawable(Color.parseColor("#4876FF"))
        pagerSlidingTabStrip.underlineHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                1f, resources.displayMetrics).toInt()
        pagerSlidingTabStrip.indicatorHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                5f, resources.displayMetrics).toInt()
        pagerSlidingTabStrip.selectedTextColor = Color.WHITE
        pagerSlidingTabStrip.textColor = Color.BLACK
        for(i in menuTitles.indices) {
            menuData.add(hashMapOf("icon" to menuIcons[i], "title" to menuTitles[i]))
        }
        leftMenuList.adapter = SimpleAdapter(this, menuData,
                R.layout.left_menu_list_item,
                arrayOf("icon", "title"),
                intArrayOf(R.id.icon, R.id.title))
    }

    private fun initData() {
        //setupFirstLaunch()
    }

    fun setupFirstLaunch() {
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("firstLaunch", false)
        editor.commit()
    }

    private fun addListener() {
        val actionBarDrawerToggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        actionBarDrawerToggle.syncState()
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        pagerSlidingTabStrip.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })
        leftMenuList.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            d(TAG, "position:" + menuData[position]["title"])
        }

        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_message -> d(TAG, "aaa")
            }
            true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when(item!!.itemId) {
//            R.id.action_message -> d(TAG, "aaa")
//        }
//        return super.onOptionsItemSelected(item)
//    }

    inner class PagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

        private val TITLES = arrayOf("主页", "联系人")

        override fun getPageTitle(position: Int): CharSequence? {
            return TITLES[position]
        }

        override fun getItem(position: Int): Fragment? {
            when (position) {
                0 -> return HomeFragment()
                1 -> return ContactFragment()
                else -> return null
            }
        }

        override fun getCount(): Int {
            return TITLES.size
        }
    }

    override fun onContactFragmentInteraction(user: User) {
        d(TAG, user.name)
    }

    companion object {
        private val TAG = "MainActivity"
    }
}

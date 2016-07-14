package com.beyebe.fastdevframe.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import com.beyebe.fastdevframe.R
import com.nightonke.wowoviewpager.*
import com.nightonke.wowoviewpager.Color.ColorChangeType
import com.nightonke.wowoviewpager.Eases.EaseType
import kotlinx.android.synthetic.main.activity_user_guide.*
import java.util.*

class UserGuideActivity : AppCompatActivity() {

    private var viewList: MutableList<View>? = null
    private var adapter: WoWoViewPagerAdapter? = null
    private var easeType = EaseType.EaseInCubic
    private var useSameEaseTypeBack = true
    var TAG = "UserGuideActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_guide)
        adapter = WoWoViewPagerAdapter(supportFragmentManager)
        adapter!!.fragmentsNumber = 3
        wowoViewPager.adapter = adapter
        setBlue1Anim()
        setGreen1Anim()
        setRed2Anim()
        setBlue2Anim()
        setRed3Anim()
        setGreen3Anim()
        setStartAppBtnAnim()
        wowoViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                Log.d(TAG, "onPageScrolled")
            }

            override fun onPageSelected(position: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        startAppBtn.setOnClickListener { toMain() }
    }

    private fun setBlue1Anim() {
        val animation = ViewAnimation(blue1)
        val blueInAnim = TranslateAnimation(0f, -WoWoUtil.dp2px(400, this).toFloat(), 0f, 0f)
        blueInAnim.fillAfter = true
        blueInAnim.duration = 1000
        blue1.startAnimation(blueInAnim)
        animation.addPageAnimaition(WoWoTranslationAnimation(
                0,
                0f,
                1f,
                blue1.translationX,
                blue1.translationY,
                -WoWoUtil.getScreenWidth(this).toFloat(),
                0f,
                easeType,
                useSameEaseTypeBack));
        wowoViewPager.addAnimation(animation)
    }

    private fun setGreen1Anim() {
        val animation = ViewAnimation(green1)
        animation.addPageAnimaition(WoWoScaleAnimation(
                0, 0f, 1f,
                12f,
                12f,
                easeType,
                useSameEaseTypeBack))
        animation.addPageAnimaition(WoWoBackgroundColorAnimation(
                1, 0f, 1f,
                resources.getColor(android.R.color.holo_green_light),
                resources.getColor(android.R.color.holo_blue_bright),
                ColorChangeType.RGB,
                easeType,
                useSameEaseTypeBack))
        wowoViewPager.addAnimation(animation)
    }

    private fun setRed2Anim() {
        val animation = ViewAnimation(red2)
        animation.addPageAnimaition(WoWoAlphaAnimation(0, 0f, 1f,
                0f,
                1f,
                easeType,
                useSameEaseTypeBack))
        animation.addPageAnimaition(WoWoTranslationAnimation(
                0,
                0f,
                1f,
                red2.translationX,
                red2.translationY,
                -WoWoUtil.dp2px(58, this).toFloat(),
                0f,
                easeType,
                useSameEaseTypeBack));
        animation.addPageAnimaition(WoWoAlphaAnimation(1, 0f, 1f,
                1f,
                0f,
                easeType,
                useSameEaseTypeBack))
        animation.addPageAnimaition(WoWoScaleAnimation(
                1, 0f, 1f,
                2f,
                2f,
                easeType,
                useSameEaseTypeBack))
        wowoViewPager.addAnimation(animation)
    }

    private fun setBlue2Anim() {
        val animation = ViewAnimation(blue2)
        animation.addPageAnimaition(WoWoAlphaAnimation(0, 0f, 1f,
                0f,
                1f,
                easeType,
                useSameEaseTypeBack))
        animation.addPageAnimaition(WoWoTranslationAnimation(
                0,
                0f,
                1f,
                blue2.translationX,
                blue2.translationY,
                WoWoUtil.dp2px(58, this).toFloat(),
                0f,
                easeType,
                useSameEaseTypeBack));
        animation.addPageAnimaition(WoWoAlphaAnimation(1, 0f, 1f,
                1f,
                0f,
                easeType,
                useSameEaseTypeBack))
        animation.addPageAnimaition(WoWoScaleAnimation(
                1, 0f, 1f,
                2f,
                2f,
                easeType,
                useSameEaseTypeBack))
        wowoViewPager.addAnimation(animation)
    }

    private fun setRed3Anim() {
        val animation = ViewAnimation(red3)
        animation.addPageAnimaition(WoWoTranslationAnimation(
                1,
                0f,
                1f,
                red3.translationX,
                red3.translationY,
                -WoWoUtil.dp2px(400, this).toFloat(),
                0f,
                easeType,
                useSameEaseTypeBack));
        wowoViewPager.addAnimation(animation)
    }

    private fun setGreen3Anim() {
        val animation = ViewAnimation(green3)
        animation.addPageAnimaition(WoWoTranslationAnimation(
                1,
                0f,
                1f,
                green3.translationX,
                green3.translationY,
                -WoWoUtil.dp2px(450, this).toFloat(),
                0f,
                easeType,
                useSameEaseTypeBack));
        wowoViewPager.addAnimation(animation)
    }

    private fun setStartAppBtnAnim() {
        val animation = ViewAnimation(startAppBtn)
        animation.addPageAnimaition(WoWoAlphaAnimation(1, 0f, 1f,
                0f,
                1f,
                easeType,
                useSameEaseTypeBack))
        wowoViewPager.addAnimation(animation)
    }

    private fun toMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private fun initData() {
        viewList = ArrayList<View>()
        val random = Random()
        for (i in 0..2) {
            val view = View(this)
            view.setBackgroundColor(-16777216 or random.nextInt(16777215))
            viewList!!.add(view)
        }
    }

    internal var pagerAdapter: PagerAdapter = object : PagerAdapter() {

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {

            return arg0 === arg1
        }

        override fun getCount(): Int {

            return viewList!!.size
        }

        override fun destroyItem(container: ViewGroup, position: Int,
                                 `object`: Any) {
            container.removeView(viewList!![position])
        }

        override fun getItemPosition(`object`: Any?): Int {
            return super.getItemPosition(`object`)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "title"
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(viewList!![position])
            return viewList!![position]
        }

    }
}

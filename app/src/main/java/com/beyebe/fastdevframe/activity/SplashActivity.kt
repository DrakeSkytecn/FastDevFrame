package com.beyebe.fastdevframe.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.beyebe.fastdevframe.R

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val handler = Handler()
        val view = View.inflate(this, R.layout.activity_splash, null)
        val animation = AnimationUtils.loadAnimation(this, R.anim.splash_fade_in)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {

            }

            override fun onAnimationEnd(animation: Animation) {
                handler.postDelayed({ checkIsFirstLaunch() }, 1000)
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        setContentView(view)
        view.startAnimation(animation)
    }

    private fun checkIsFirstLaunch() {
        Log.d(TAG, "SDK_INT:"+Build.VERSION.SDK_INT)
        val sharedPreferences = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)
        val firstLaunch = sharedPreferences.getBoolean("firstLaunch", true)
        if (firstLaunch) {
            toUserGuide()
        } else {
            startApp()
        }
    }

    private fun toUserGuide(){
        startActivity(Intent(this, UserGuideActivity::class.java))
        finish()
    }

    private fun startApp() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

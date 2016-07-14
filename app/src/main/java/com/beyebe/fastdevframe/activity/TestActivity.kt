package com.beyebe.fastdevframe.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.KeyEvent
import android.view.MenuItem
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.util.AppTool

class TestActivity : AppCompatActivity() {

    private var toolbar: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)
        initData()
        initView()
        addListener()
        findViewById(R.id.button2)!!.setOnClickListener { AppTool.closeDefaultAnimationActivity(this) }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            AppTool.closeDefaultAnimationActivity(this)
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.repeatCount == 0) {
            AppTool.closeDefaultAnimationActivity(this)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun initData() {

    }

    private fun initView() {
        toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        toolbar!!.setTitle(R.string.title_activity_test)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun addListener() {

    }
}

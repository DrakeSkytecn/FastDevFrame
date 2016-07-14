package com.beyebe.fastdevframe

import android.app.Application

import com.zhy.http.okhttp.OkHttpUtils

import java.util.concurrent.TimeUnit

/**
 * Created by Kratos on 2016/1/20.
 */
class FastDevFrameApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        OkHttpUtils.getInstance().setConnectTimeout(OkHttpUtils.DEFAULT_MILLISECONDS.toInt(), TimeUnit.MILLISECONDS)
    }
}

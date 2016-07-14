package com.beyebe.fastdevframe.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.beyebe.fastdevframe.R

/**
 * Created by Kratos on 2016/1/22.
 */
object AppTool {

    fun openDefaultAnimationActivity(context: Context, cls: Class<*>) {
        context.startActivity(Intent(context, cls))
        (context as Activity).overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    fun closeDefaultAnimationActivity(context: Context) {
        val activity = context as Activity
        activity.finish()
        activity.overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}

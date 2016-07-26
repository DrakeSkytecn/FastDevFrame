package com.beyebe.fastdevframe.util

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import com.beyebe.fastdevframe.R
import java.util.regex.Pattern

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

    fun systemPhoneCall(context: Context, phoneNumber: String) {
        context.startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber)))
    }

    fun openAppSettings(context: Context) {
        context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + context.packageName)))
    }

    fun isPhoneNumber(number: String): Boolean {
        val pattern = Pattern.compile("^\\d{5,13}$")
        val matcher = pattern.matcher(number)
        return matcher.matches()
    }
}

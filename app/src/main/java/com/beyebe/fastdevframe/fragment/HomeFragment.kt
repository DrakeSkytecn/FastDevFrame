package com.beyebe.fastdevframe.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.activity.RootActivity
import com.beyebe.fastdevframe.activity.TestActivity
import com.beyebe.fastdevframe.model.Home
import com.beyebe.fastdevframe.util.AppTool
import com.beyebe.fastdevframe.util.CircleTransform
import com.beyebe.fastdevframe.util.Constant
import com.bigkoo.svprogresshud.SVProgressHUD
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import com.zhy.http.okhttp.OkHttpUtils
import com.zhy.http.okhttp.callback.StringCallback
import kotlinx.android.synthetic.main.fragment_home.*
import okhttp3.Call

class HomeFragment : Fragment() {

    private var homeData: Home? = null
    private var progress: SVProgressHUD? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val contentView = inflater!!.inflate(R.layout.fragment_home, container, false)
        progress = SVProgressHUD(context)
        progress!!.showWithStatus("加载中...", SVProgressHUD.SVProgressHUDMaskType.BlackCancel)
        return contentView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadHomeData()
        addListener()
    }

    private fun loadHomeData() {
        OkHttpUtils.get().url(Constant.BASE_URL).addParams("type", "1").addParams("filter", "0,4").build().execute(object : StringCallback() {
            override fun onError(call: Call, e: Exception) {
                loadHomeData()
            }

            override fun onResponse(response: String) {
                try {
                    homeData = Gson().fromJson<Home>(response, Home::class.java)
                } catch (e: Exception) {

                }
            }

            override fun onAfter() {
                super.onAfter()
                updateView()
                progress!!.dismiss()
            }
        })
    }

    private fun updateView() {
        if (context != null && homeData != null) {
            productName.text = homeData!!.DefaultKeyword
            val ads = homeData!!.AdsFocusList
            if (ads != null) {
                Picasso.with(context).load(ads[0].Picture).placeholder(R.mipmap.default_icon).transform(CircleTransform()).into(productView)
            }
        }
    }

    fun addListener() {
        productView.setOnClickListener {
            AppTool.openDefaultAnimationActivity(activity, RootActivity::class.java)
        }
    }
}

package com.beyebe.fastdevframe.fragment

import android.Manifest
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.anthonycr.grant.PermissionsManager
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.adapter.DialPlateAdapter
import com.beyebe.fastdevframe.util.AppTool
import com.beyebe.fastdevframe.view.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.dial_plate_item_a.view.*
import kotlinx.android.synthetic.main.fragment_dial_plate.*

/**
 * A simple [Fragment] subclass.
 */
class DialPlateFragment : Fragment(), DialPlateAdapter.OnDialPlateItemClickListener {

    private val TAG: String? = "DialPlateFragment"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dial_plate, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dial_plate.layoutManager = GridLayoutManager(context, 3)
        dial_plate.addItemDecoration(DividerGridItemDecoration(context))
        dial_plate.adapter = DialPlateAdapter(this)
        callBtn.setOnClickListener {
            if (PermissionsManager.hasPermission(context, Manifest.permission.CALL_PHONE)) {
                AppTool.systemPhoneCall(context, dialNumber.text.toString())
            } else {
                showWhyPhoneCallPermission()
            }
        }
    }

    fun showWhyPhoneCallPermission() {
        AlertDialog.Builder(context)
                .setMessage("使用拨打电话功能需要您的授权, 请在设置中开启拨打电话的权限")
                .setPositiveButton("确定") { dialog, which ->
                    AppTool.openAppSettings(context)
                }.setNegativeButton("取消", null)
                .create()
                .show()
    }

    override fun onItemClick(view: View, position: Int) {
        when (position) {
            in 0..8, 10 -> {
                var temp = dialNumber.text.toString()
                if (temp.length < 12) {
                    temp += view.number.text
                    dialNumber.text = temp
                    if (!dialNumberCon.isShown) {
                        dialNumberCon.visibility = View.VISIBLE
                    }
                }
            }
            9 -> {
                val clip = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                if (clip.primaryClip != null) {
                    Log.d(TAG, clip.primaryClip.toString())
                    val phoneNumber = clip.primaryClip.getItemAt(0).text.toString()
                    if (AppTool.isPhoneNumber(phoneNumber)) {
                        dialNumber.text = phoneNumber
                        dialNumberCon.visibility = View.VISIBLE
                    }
                }
            }
            11 -> {
                var temp = dialNumber.text.toString()
                if (temp.length > 0) {
                    temp = temp.substring(0..(temp.length - 2))
                    dialNumber.text = temp
                    if (temp.length == 0){
                        dialNumberCon.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}// Required empty public constructor

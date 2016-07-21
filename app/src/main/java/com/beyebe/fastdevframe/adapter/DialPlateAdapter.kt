package com.beyebe.fastdevframe.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.beyebe.fastdevframe.R
import kotlinx.android.synthetic.main.dial_plate_item_a.view.*
import kotlinx.android.synthetic.main.dial_plate_item_b.view.*

/**
 * Created by daiquanyi on 16/7/20.
 */
class DialPlateAdapter(private val onDialPlateItemClickListener: OnDialPlateItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val characters = arrayOf("ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ")

    enum class ITEM_TYPE {
        ITEMA,
        ITEMB
    }

    override fun getItemViewType(position: Int): Int {
        when (position) {
            in 0..8, 10 -> return ITEM_TYPE.ITEMA.ordinal
            9, 11 -> return ITEM_TYPE.ITEMB.ordinal
            else -> return super.getItemViewType(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder: RecyclerView.ViewHolder
        val view: View
        var typedValue = TypedValue()
        (parent.context as Activity).theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
        if (viewType == ITEM_TYPE.ITEMA.ordinal) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.dial_plate_item_a, parent, false)
            //view.setBackgroundResource(R.drawable.abc_item_background_holo_light)
            viewHolder = ItemAViewHolder(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.dial_plate_item_b, parent, false)
            //view.setBackgroundResource(R.drawable.abc_item_background_holo_light)
            viewHolder = ItemBViewHolder(view)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        var mHolder: RecyclerView.ViewHolder? = null
        when (position) {
            in 0..8, 10 -> {
                mHolder = holder as ItemAViewHolder
                if (position == 0) {
                    mHolder.number.text = (position + 1).toString()
                } else if (position == 10) {
                    mHolder.number.text = "0"
                } else {
                    mHolder.number.text = (position + 1).toString()
                    mHolder.character.text = characters[position - 1]
                }
            }
            9, 11 -> {
                mHolder = holder as ItemBViewHolder
                if (position == 9) {
                    mHolder.icon.setImageResource(R.mipmap.paste)
                    mHolder.hint.text = "粘贴"
                } else {
                    mHolder.icon.setImageResource(R.mipmap.delete)
                    mHolder.hint.text = "删除"
                }
            }
        }
        mHolder!!.itemView.setOnClickListener {
            onDialPlateItemClickListener.onItemClick(mHolder!!.itemView, position)
        }
    }

    override fun getItemCount(): Int {
        return 12
    }

    inner class ItemAViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val number: TextView
        val character: TextView

        init {
            number = mView.number
            character = mView.character
        }
    }

    inner class ItemBViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val icon: ImageView
        val hint: TextView

        init {
            icon = mView.icon
            hint = mView.hint
        }
    }

    interface OnDialPlateItemClickListener {

        fun onItemClick(view: View, position: Int)
    }

}
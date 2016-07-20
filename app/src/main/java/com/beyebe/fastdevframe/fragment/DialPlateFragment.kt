package com.beyebe.fastdevframe.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.adapter.DialPlateAdapter
import com.beyebe.fastdevframe.view.widget.DividerGridItemDecoration
import kotlinx.android.synthetic.main.fragment_dial_plate.*


/**
 * A simple [Fragment] subclass.
 */
class DialPlateFragment : Fragment(), DialPlateAdapter.OnDialPlateItemClickListener {

    private val  TAG: String? = "DialPlateFragment"

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_dial_plate, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dial_plate.layoutManager = GridLayoutManager(context, 3)
        dial_plate.addItemDecoration(DividerGridItemDecoration(context))
        dial_plate.adapter = DialPlateAdapter(this)
    }

    override fun onItemClick(position: Int) {
        Log.d(TAG, ""+position)
    }

}// Required empty public constructor

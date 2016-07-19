package com.beyebe.fastdevframe.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.beyebe.fastdevframe.R
import kotlinx.android.synthetic.main.fragment_dial_plate.*


/**
 * A simple [Fragment] subclass.
 */
class DialPlateFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater!!.inflate(R.layout.fragment_dial_plate, container, false)
    }

}// Required empty public constructor

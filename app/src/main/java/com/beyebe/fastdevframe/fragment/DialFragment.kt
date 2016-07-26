package com.beyebe.fastdevframe.fragment

import android.animation.ObjectAnimator
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.adapter.DialCallViewPagerAdapter
import com.nightonke.wowoviewpager.WoWoUtil
import kotlinx.android.synthetic.main.fragment_dial.*

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [DialFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [DialFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialFragment : Fragment() {

    private val TAG: String? = "DialFragment"

    private var isDialShow: Boolean = true
    private var isDialViewHidden: Boolean = false

    // TODO: Rename and change types of parameters
    private var mParam1: String? = null
    private var mParam2: String? = null

    private var mListener: OnFragmentInteractionListener? = null
    private var dialCallViewPagerAdapter: DialCallViewPagerAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments.getString(ARG_PARAM1)
            mParam2 = arguments.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_dial, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialCallViewPagerAdapter = DialCallViewPagerAdapter(fragmentManager)
        dial_view_pager.adapter = dialCallViewPagerAdapter
        segment_control.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.call_segment -> dial_view_pager.setCurrentItem(0, false)
                R.id.contact_segment -> dial_view_pager.setCurrentItem(1, false)
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        Log.d(TAG, "onHiddenChanged")
    }

    private fun showDial() {
        dialAnimationType(ObjectAnimator.ofFloat(dial_view, "translationY", 0f))
        isDialShow = true
    }

    private fun hideDial() {
        if (view == null) {
            Log.d(TAG, "DialFragment view == null")
        }
        dialAnimationType(ObjectAnimator.ofFloat(dial_view, "translationY", WoWoUtil.dp2px(265, activity).toFloat()))
        isDialShow = false
    }

    private fun dialAnimationType(animation: ObjectAnimator) {
        animation.duration = resources.getInteger(android.R.integer.config_mediumAnimTime).toLong()
        animation.start()
    }

    fun changeDialState(item: MenuItem) {
        if (!isDialShow) {
            item.setIcon(R.mipmap.dial_down)
            item.setTitle(R.string.menu_1_1)
            showDial()
        } else {
            item.setIcon(R.mipmap.dial_up)
            item.setTitle(R.string.menu_1_2)
            hideDial()
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        if (mListener != null) {
            mListener!!.onFragmentInteraction(uri)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context as OnFragmentInteractionListener?
        } else {
            //            throw new RuntimeException(context.toString()
            //                    + " must implement OnFragmentInteractionListener");
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.

         * @param param1 Parameter 1.
         * *
         * @param param2 Parameter 2.
         * *
         * @return A new instance of fragment DialFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(param1: String, param2: String): DialFragment {
            val fragment = DialFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putString(ARG_PARAM2, param2)
            fragment.arguments = args
            return fragment
        }
    }
}// Required empty public constructor

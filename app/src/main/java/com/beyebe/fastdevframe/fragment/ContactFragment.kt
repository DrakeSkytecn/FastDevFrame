package com.beyebe.fastdevframe.fragment

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.view.animation.OvershootInterpolator
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.adapter.ContactRecyclerViewAdapter
import com.beyebe.fastdevframe.model.User
import com.beyebe.fastdevframe.util.EndlessRecyclerOnScrollListener
import com.beyebe.fastdevframe.view.widget.DividerItemDecoration
import com.beyebe.fastdevframe.view.widget.LoadMoreRecyclerView
import io.realm.Realm
import jp.wasabeef.recyclerview.animators.FadeInAnimator
import jp.wasabeef.recyclerview.animators.FadeInDownAnimator
import kotlinx.android.synthetic.main.fragment_contact.*

class ContactFragment : Fragment() {

    private val TAG: String? = "ContactFragment"
    private var contactRecyclerViewAdapter: ContactRecyclerViewAdapter? = null
    private var mListener: OnContactFragmentInteractionListener? = null
    private var realm: Realm? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        realm = Realm.getInstance(context)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_contact, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        contactRecyclerViewAdapter = ContactRecyclerViewAdapter(realm?.where(User::class.java)!!.findAll(), mListener)
        loadMoreRecyclerView.layoutManager = LinearLayoutManager(context)
        loadMoreRecyclerView.itemAnimator = FadeInAnimator(OvershootInterpolator(0.5f))
        loadMoreRecyclerView.addItemDecoration(DividerItemDecoration(context, null))
        loadMoreRecyclerView.adapter = contactRecyclerViewAdapter
        loadMoreRecyclerView.setAutoLoadMoreEnable(true)

        swipeRefreshLayout.setOnRefreshListener {
            Handler().postDelayed({
                contactRecyclerViewAdapter?.users = realm?.where(User::class.java)!!.findAll()
                contactRecyclerViewAdapter?.notifyDataSetChanged()
                loadMoreRecyclerView.scrollToPosition(0)
                swipeRefreshLayout.isRefreshing = false
            }, 2000)
        }
                loadMoreRecyclerView.setLoadMoreListener(object : LoadMoreRecyclerView.LoadMoreListener {

                    override fun onLoadMore() {
                        Handler().postDelayed({
                            for (i in 1..5) {
                                addUser()
                            }
                            contactRecyclerViewAdapter?.users = realm?.where(User::class.java)!!.findAll()
                            loadMoreRecyclerView.notifyMoreFinish(true)
                        }, 2000)
                    }
                })

        addBtn.setOnClickListener {
            //.loadMoreRecyclerView?.setAutoLoadMoreEnable(true)
            addUser()
            contactRecyclerViewAdapter?.users = realm?.where(User::class.java)!!.findAll()
            contactRecyclerViewAdapter?.notifyItemInserted(contactRecyclerViewAdapter?.users!!.size - 1)
            loadMoreRecyclerView.scrollToPosition(contactRecyclerViewAdapter?.users!!.size - 1)
        }

        deleteBtn.setOnClickListener {
            contactRecyclerViewAdapter?.notifyItemRangeRemoved(0, contactRecyclerViewAdapter?.users!!.size - 1)
            loadMoreRecyclerView.scrollToPosition(0)
            deleteAllUsersData()
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnContactFragmentInteractionListener) {
            mListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    private fun addUser() {
        realm?.beginTransaction()
        var user = realm?.createObject(User::class.java)
        user?.name = "Lebron James"
        user?.age = 30
        realm?.commitTransaction()
    }

    private fun deleteAllUsersData() {
        realm?.beginTransaction()
        realm?.clear(User::class.java)
        realm?.commitTransaction()
    }

    interface OnContactFragmentInteractionListener {

        fun onContactFragmentInteraction(user: User)
    }
}
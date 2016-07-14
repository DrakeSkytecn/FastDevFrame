package com.beyebe.fastdevframe.adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.beyebe.fastdevframe.R
import com.beyebe.fastdevframe.fragment.ContactFragment
import com.beyebe.fastdevframe.model.User

class ContactRecyclerViewAdapter(private var mUsers: List<User>, private val mListener: ContactFragment.OnContactFragmentInteractionListener?) : RecyclerView.Adapter<ContactRecyclerViewAdapter.ViewHolder>() {

    private val TAG = "ContactRecyclerViewAdapter"
    var users: List<User>
        get() = mUsers
        set(value) {
            mUsers = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false)
        var typedValue = TypedValue()
        (mListener as Activity).theme.resolveAttribute(R.attr.selectableItemBackground, typedValue, true)
        view.setBackgroundResource(typedValue.resourceId)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.age.text = mUsers[position].age.toString()
        holder.name.text = mUsers[position].name
        holder.mView.setOnClickListener {
            mListener?.onContactFragmentInteraction(mUsers[position])
        }
    }

    override fun getItemCount(): Int {
        return mUsers.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {

        val age: TextView
        val name: TextView

        init {
            age = mView.findViewById(R.id.age) as TextView
            name = mView.findViewById(R.id.name) as TextView
        }
    }
}

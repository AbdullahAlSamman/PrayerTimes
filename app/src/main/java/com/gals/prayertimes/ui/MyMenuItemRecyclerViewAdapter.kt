package com.gals.prayertimes.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gals.prayertimes.R
import com.gals.prayertimes.ui.MenuItemFragment.OnListFragmentInteractionListener
import com.gals.prayertimes.ui.SettingsMenuContent.SettingsMenuItem

/**
 * [RecyclerView.Adapter] that can display a [SettingsMenuItem] and makes a call to the
 * specified [OnListFragmentInteractionListener].
 */
class MyMenuItemRecyclerViewAdapter(
    private val mValues: List<SettingsMenuItem>,
    private val mListener: OnListFragmentInteractionListener?
) : RecyclerView.Adapter<MyMenuItemRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(
                R.layout.fragment_menuitem,
                parent,
                false
            )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.mItem = mValues[position]
        holder.mImage.setImageResource(mValues[position].img)
        holder.mNameView.text = mValues[position].name
        holder.mView.setOnClickListener { v ->
            if (mListener != null) {
                Log.i(
                    "List",
                    v.id.toString() + "  " + holder.mNameView
                )
                mListener.onListFragmentInteraction(holder.mItem)
            }
        }
    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        val mImage = mView.findViewById<View>(R.id.item_image) as ImageView
        val mNameView = mView.findViewById<View>(R.id.name) as TextView
        lateinit var mItem: SettingsMenuItem
        override fun toString(): String {
            return super.toString() + " '" + mNameView.text + "'"
        }
    }
}
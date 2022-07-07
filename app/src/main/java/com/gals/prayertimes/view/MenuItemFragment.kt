package com.gals.prayertimes.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gals.prayertimes.R
import com.gals.prayertimes.view.MenuItemFragment.OnListFragmentInteractionListener
import com.gals.prayertimes.view.SettingsMenuContent.SettingsMenuItem

/**
 * A fragment representing a list of Items.
 *
 * Activities containing this fragment MUST implement the [OnListFragmentInteractionListener]
 * interface.
 *
 * Mandatory empty constructor for the fragment manager to instantiate the
 * fragment (e.g. upon screen orientation changes).
 */
class MenuItemFragment : Fragment() {
    private var mColumnCount = 1
    private var mListener: OnListFragmentInteractionListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mColumnCount = requireArguments().getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(
            R.layout.fragment_menuitem_list,
            container,
            false
        )
        val items: MutableList<SettingsMenuItem> = ArrayList()
        val m1 = SettingsMenuItem(
            1,
            R.drawable.notifications_active_black_48,
            getString(R.string.text_alarm_settings)
        )
        val m2 = SettingsMenuItem(
            2,
            R.drawable.import_contacts_black_48,
            getString(R.string.text_settings_privacy_policy)
        )
        val m3 = SettingsMenuItem(
            3,
            R.drawable.info_black_48,
            getString(R.string.text_settings_about_us)
        )
        items.add(m1)
        items.add(m2)
        items.add(m3)

        /**Set the adapter*/
        if (view is RecyclerView) {
            val context = view.getContext()
            val recyclerView = view
            if (mColumnCount <= 1) {
                recyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                recyclerView.layoutManager = GridLayoutManager(
                    context,
                    mColumnCount
                )
            }
            recyclerView.adapter = MyMenuItemRecyclerViewAdapter(
                items,
                mListener
            )
            recyclerView.addOnItemTouchListener(
                RecyclerItemClickListener(
                    getContext(),
                    recyclerView,
                    object : RecyclerItemClickListener.OnItemClickListener {
                        override fun onItemClick(
                            view: View?,
                            position: Int
                        ) {
                            val athanFrag = AthanSettingsFragment()
                            val ppFrag = PrivacyPolicyFragment()
                            when (position) {
                                0 -> {
                                    Log.i(
                                        "List",
                                        "Item Click $position"
                                    )
                                    activity!!.supportFragmentManager.beginTransaction()
                                        .replace(
                                            R.id.fragmentContainer,
                                            athanFrag,
                                            ""
                                        )
                                        .addToBackStack(null)
                                        .commit()
                                }
                                1 -> {
                                    Log.i(
                                        "List",
                                        "Item Click $position"
                                    )
                                    activity!!.supportFragmentManager.beginTransaction()
                                        .replace(
                                            R.id.fragmentContainer,
                                            ppFrag,
                                            ""
                                        )
                                        .addToBackStack(null)
                                        .commit()
                                }
                                2 -> Log.i(
                                    "List",
                                    "Item Click $position"
                                )
                                else -> {}
                            }
                            Log.i(
                                "List",
                                "Item Click $position"
                            )
                        }

                        override fun onLongItemClick(
                            view: View?,
                            position: Int
                        ) {
                        }
                    }
                )
            )
        }
        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnListFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                    + " must implement OnListFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener.run { }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * See the Android Training lesson [Communicating with Other Fragments](http://developer.android.com/training/basics/fragments/communicating.html) for more information.
     */
    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SettingsMenuItem?)
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
        fun newInstance(columnCount: Int): MenuItemFragment {
            val fragment = MenuItemFragment()
            val args = Bundle()
            args.putInt(
                ARG_COLUMN_COUNT,
                columnCount
            )
            fragment.arguments = args
            return fragment
        }
    }
}
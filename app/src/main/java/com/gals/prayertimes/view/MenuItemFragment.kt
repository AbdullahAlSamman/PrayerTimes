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
import com.gals.prayertimes.adapter.MyMenuItemRecyclerViewAdapter
import com.gals.prayertimes.adapter.RecyclerItemClickListener
import com.gals.prayertimes.adapter.SettingsMenuContent.SettingsMenuItem

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
        val items: MutableList<SettingsMenuItem> = arrayListOf(
            SettingsMenuItem(
                1,
                R.drawable.notifications_active_black_48,
                getString(R.string.text_alarm_settings)
            ),
            SettingsMenuItem(
                2,
                R.drawable.import_contacts_black_48,
                getString(R.string.text_settings_privacy_policy)
            ),
            SettingsMenuItem(
                3,
                R.drawable.info_black_48,
                getString(R.string.text_settings_about_us)
            )
        )

        /*TODO: see a modern alternative*/
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

    interface OnListFragmentInteractionListener {
        fun onListFragmentInteraction(item: SettingsMenuItem?)
    }

    companion object {
        private const val ARG_COLUMN_COUNT = "column-count"
    }
}
package com.gals.prayertimes.ui;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gals.prayertimes.R;
import com.gals.prayertimes.ui.SettingsMenuContent.SettingsMenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class MenuItemFragment extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MenuItemFragment() {
    }


    public static MenuItemFragment newInstance(int columnCount) {
        MenuItemFragment fragment = new MenuItemFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menuitem_list, container, false);


        List<SettingsMenuItem> items = new ArrayList<>();

        SettingsMenuItem m1 = new SettingsMenuItem(1, R.drawable.notifications_active_black_48, getString(R.string.alarmSettings));
        SettingsMenuItem m2 = new SettingsMenuItem(2, R.drawable.import_contacts_black_48, getString(R.string.settingsPrivacyPolicy));
        SettingsMenuItem m3 = new SettingsMenuItem(3, R.drawable.info_black_48, getString(R.string.settingsAboutUs));

        items.add(m1);
        items.add(m2);
        items.add(m3);


        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyMenuItemRecyclerViewAdapter(items, mListener));

            recyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            AthanSettingsFragment athanFrag = new AthanSettingsFragment();
                            PrivacyPolicyFragment ppFrag = new PrivacyPolicyFragment();

                            switch (position) {
                                case 0:
                                    Log.i("List", "Item Click " + position);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragmentContainer, athanFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                    break;
                                case 1:
                                    Log.i("List", "Item Click " + position);
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.fragmentContainer, ppFrag, "")
                                            .addToBackStack(null)
                                            .commit();
                                    break;
                                case 2:
                                    Log.i("List", "Item Click " + position);
                                    break;
                                default:
                                    break;
                            }
                            Log.i("List", "Item Click " + position);
                        }

                        @Override
                        public void onLongItemClick(View view, int position) {
                            // do whatever
                        }
                    })
            );

        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(SettingsMenuItem item);
    }

}

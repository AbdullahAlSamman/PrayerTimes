package com.gals.prayertimes.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Switch;

import com.gals.prayertimes.model.Prayer;
import com.gals.prayertimes.services.PrayersODNotificationService;
import com.gals.prayertimes.R;
import com.gals.prayertimes.utils.ToolsManager;
import com.gals.prayertimes.services.MusicPlayer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AthanSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AthanSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AthanSettingsFragment extends Fragment {
    public static final String PREFS_NAME = "MyLocalStorage";
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Intent musicFullAthanIntent;
    Intent musicHalfAthanIntent;
    ToolsManager tools;
    SharedPreferences settings;
    CustomRadioGroup radioGroup;
    Prayer prayer;
    RadioButton radioFullAthan;
    RadioButton radioHalfAthan;
    RadioButton radioToneAthan;
    RadioButton radioSilentAthan;
    ImageButton playFullAthan;
    ImageButton playHalfAthan;
    Switch turnOnAlarm;

    private Boolean playerFullAthanOn = false;
    private Boolean playerHalfAthanOn = false;

    //Default Params
    private String mParam1;
    private String mParam2;
    private OnFragmentInteractionListener mListener;

    public AthanSettingsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AthanSettingsFragment.
     */
    //Rename and change types and number of parameters
    public static AthanSettingsFragment newInstance(String param1, String param2) {
        AthanSettingsFragment fragment = new AthanSettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public Boolean getPlayerHalfAthanOn() {
        return playerHalfAthanOn;
    }

    public void setPlayerHalfAthanOn(Boolean playerHalfAthanOn) {
        this.playerHalfAthanOn = playerHalfAthanOn;
    }

    public Boolean getPlayerFullAthanOn() {
        return playerFullAthanOn;
    }

    public void setPlayerFullAthanOn(Boolean playerFullAthanOn) {
        this.playerFullAthanOn = playerFullAthanOn;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        // View Elements id's
        radioFullAthan = (RadioButton) view.findViewById(R.id.radioFullAthan);
        radioHalfAthan = (RadioButton) view.findViewById(R.id.radioHalfAthan);
        radioToneAthan = (RadioButton) view.findViewById(R.id.radioToneAthan);
        radioSilentAthan = (RadioButton) view.findViewById(R.id.radioSilentAthan);

        playFullAthan = (ImageButton) view.findViewById(R.id.playFullAthan);
        playHalfAthan = (ImageButton) view.findViewById(R.id.playHalfAthan);

        turnOnAlarm = (Switch) view.findViewById(R.id.switchAlarmOn);

        settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
        tools = new ToolsManager(getContext());
        prayer = new Prayer(settings, getContext());
        prayer.getLocalStorage();
        radioGroup = new CustomRadioGroup(view, R.id.radioFullAthan, R.id.radioHalfAthan, R.id.radioToneAthan, R.id.radioSilentAthan);

        turnOnAlarm.setChecked(prayer.getNotificaiton());
        if (!turnOnAlarm.isChecked())
            radioGroup.setAllDisabled();

        switch (prayer.getNotificaitonType()) {
            case "full":
                radioFullAthan.setChecked(true);
                break;
            case "half":
                radioHalfAthan.setChecked(true);
                break;
            case "tone":
                radioToneAthan.setChecked(true);
                break;
            case "silent":
                radioSilentAthan.setChecked(true);
                break;
            default:
                break;
        }

        turnOnAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (turnOnAlarm.isChecked()) {
                    radioGroup.setAllEnabled();
                } else {
                    radioGroup.setAllDisabled();
                }
            }
        });

        musicFullAthanIntent = new Intent(getContext(), MusicPlayer.class);
        musicFullAthanIntent.putExtra("athanType", R.raw.fullathan);

        musicHalfAthanIntent = new Intent(getContext(), MusicPlayer.class);
        musicHalfAthanIntent.putExtra("athanType", R.raw.halfathan);

        playFullAthan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getPlayerFullAthanOn()) {
                    setPlayerFullAthanOn(true);
                    playHalfAthan.setEnabled(false);
                    getContext().startService(musicFullAthanIntent);
                    playFullAthan.setImageResource(android.R.drawable.ic_media_pause);
                } else if (getPlayerFullAthanOn()) {
                    setPlayerFullAthanOn(false);
                    playHalfAthan.setEnabled(true);
                    getContext().stopService(musicFullAthanIntent);
                    playFullAthan.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });

        playHalfAthan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!getPlayerHalfAthanOn()) {
                    playFullAthan.setEnabled(false);
                    setPlayerHalfAthanOn(true);
                    getContext().startService(musicHalfAthanIntent);
                    playHalfAthan.setImageResource(android.R.drawable.ic_media_pause);
                } else if (getPlayerHalfAthanOn()) {
                    setPlayerHalfAthanOn(false);
                    playFullAthan.setEnabled(true);
                    getContext().stopService(musicHalfAthanIntent);
                    playHalfAthan.setImageResource(android.R.drawable.ic_media_play);
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        //Save the Values and Stop Music if running
        try {
            prayer.setNotificaiton(turnOnAlarm.isChecked());
            if (!prayer.getNotificaiton())
                startStopNotificationService(false);
            else
                startStopNotificationService(true);

            if (radioGroup.getSelectedRadio() != null) {
                switch (radioGroup.getSelectedRadio().getContentDescription().toString()) {
                    case "full":
                        prayer.setNotificaitonType("full");
                        break;
                    case "half":
                        prayer.setNotificaitonType("half");
                        break;
                    case "tone":
                        prayer.setNotificaitonType("tone");
                        break;
                    case "silent":
                        prayer.setNotificaitonType("silent");
                        break;
                    default:
                        break;
                }
            }
            prayer.setLocalStorage(settings);
            prayer.printTest();
            if (tools.isServiceRunning(MusicPlayer.class)) {
                getContext().stopService(musicFullAthanIntent);
                getContext().stopService(musicHalfAthanIntent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void startStopNotificationService(Boolean start) {
        //TODO: check if useful and change
        try {
            if (start == false) {
                Intent pService = new Intent(getContext(), PrayersODNotificationService.class);
                getContext().stopService(pService);
            } else {
                Intent pService = new Intent(getContext(), PrayersODNotificationService.class);
                getContext().startService(pService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}


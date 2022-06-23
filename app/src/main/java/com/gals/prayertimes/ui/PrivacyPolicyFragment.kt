package com.gals.prayertimes.ui

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.gals.prayertimes.R

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [PrivacyPolicyFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [PrivacyPolicyFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PrivacyPolicyFragment : Fragment() {
    lateinit var ppWebView: WebView

    // TODO: Rename and change types of parameters
    private lateinit var mParam1: String
    private lateinit var mParam2: String
    private lateinit var mListener: OnFragmentInteractionListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = requireArguments().getString(ARG_PARAM1).toString()
            mParam2 = requireArguments().getString(ARG_PARAM2).toString()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(
            R.layout.fragment_privacy_policy,
            container,
            false
        )
        ppWebView = view.findViewById(R.id.privacy_policy_webview)
        ppWebView.webViewClient = WebViewClient()
        ppWebView.loadUrl("file:///android_asset/privacy_policy.html")
        return view
    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri?) {
        mListener.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mListener = if (context is OnFragmentInteractionListener) {
            context
        } else {
            throw RuntimeException(
                context.toString()
                    + " must implement OnFragmentInteractionListener"
            )
        }
    }

    override fun onDetach() {
        super.onDetach()
        mListener.run {}
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
        fun onFragmentInteraction(uri: Uri?)
    }

    companion object {
        // TODO: Rename parameter arguments, choose names that match
        // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PrivacyPolicyFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(
            param1: String?,
            param2: String?
        ): PrivacyPolicyFragment {
            val fragment = PrivacyPolicyFragment()
            val args = Bundle()
            args.putString(
                ARG_PARAM1,
                param1
            )
            args.putString(
                ARG_PARAM2,
                param2
            )
            fragment.arguments = args
            return fragment
        }
    }
}
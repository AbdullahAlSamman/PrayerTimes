package com.gals.prayertimes.adapter

import android.view.View
import android.webkit.WebView
import androidx.databinding.BindingAdapter

object BindingAdapters {

    @JvmStatic
    @BindingAdapter("loadUrl")
    fun WebView.setUrl(url: String) {
        this.loadUrl(url)
    }

    @JvmStatic
    @BindingAdapter("app:goneUnless")
    fun goneUnless(view: View, visible: Boolean) {
        view.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
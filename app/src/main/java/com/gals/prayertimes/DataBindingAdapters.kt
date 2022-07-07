package com.gals.prayertimes

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt

class DataBindingAdapters {

    @BindingAdapter("android:src")
    fun setImageResource(
        imageView: ImageView,
        resource: ObservableInt
    ) {
        imageView.setImageResource(resource.get())
    }
}
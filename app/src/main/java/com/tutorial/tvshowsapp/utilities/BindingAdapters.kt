package com.tutorial.tvshowsapp.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import java.lang.Exception

class BindingAdapters {
    companion object {
        @BindingAdapter("android:imageURL")
        @kotlin.jvm.JvmStatic
        fun setImageURL(imageView: ImageView, URL: String) {
            try {
                imageView.load(URL)
            } catch (ignored: Exception) { }
        }
    }
}
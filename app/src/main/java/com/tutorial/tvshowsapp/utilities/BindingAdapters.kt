package com.tutorial.tvshowsapp.utilities

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import com.tutorial.tvshowsapp.R

class BindingAdapters {
    companion object {
        @BindingAdapter("android:imageURL")
        @kotlin.jvm.JvmStatic
        fun setImageURL(imageView: ImageView, URL: String?) {
            try {
                if (URL != null) {
                    imageView.load(URL) {
                        placeholder(R.mipmap.ic_launcher)
                        error(R.mipmap.ic_launcher)
                    }
                    imageView.visibility = View.VISIBLE
                } else
                    imageView.visibility = View.INVISIBLE
            } catch (ignored: Exception) { }
        }

        @BindingAdapter("android:setTextVisibility")
        @kotlin.jvm.JvmStatic
        fun setTextVisibility(textView: TextView, msg: String? = "") {
            try {
                if (msg!!.isEmpty())
                    textView.visibility = View.INVISIBLE
                else
                    textView.visibility = View.VISIBLE
            } catch (ignored: Exception) {}
        }
    }
}
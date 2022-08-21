package com.tutorial.tvshowsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.databinding.ItemContainerSliderImageBinding

class ImageSliderAdapter(private val sliderImage: MutableList<String>):
    RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageSliderViewHolder {
        return ImageSliderViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_container_slider_image,
                parent, false) as ItemContainerSliderImageBinding
        )
    }

    override fun onBindViewHolder(holder: ImageSliderViewHolder, position: Int) {
        holder.bindSliderImage(sliderImage[position])
    }

    override fun getItemCount(): Int = sliderImage.size

    class ImageSliderViewHolder(itemView: ItemContainerSliderImageBinding)
        : RecyclerView.ViewHolder(itemView.root) {

        private val binding: ItemContainerSliderImageBinding = itemView

        fun bindSliderImage(imageURL: String) {
            binding.imageURL = imageURL
        }
    }
}
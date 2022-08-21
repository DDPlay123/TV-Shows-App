package com.tutorial.tvshowsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.databinding.ItemContainerTvShowsBinding

class TVShowsAdapter(private val tvShows: MutableList<TVShows>, private val tvShowsListener: TVShowsListener)
    : RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        return TVShowViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_container_tv_shows,
                parent, false) as ItemContainerTvShowsBinding
        )
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        holder.bindTVShow(tvShows[position])
        holder.binding.root.setOnClickListener {
            tvShowsListener.onTVShowClicked(tvShows[position])
        }
    }

    override fun getItemCount(): Int = tvShows.size

    class TVShowViewHolder(itemView: ItemContainerTvShowsBinding)
        : RecyclerView.ViewHolder(itemView.root) {

        val binding: ItemContainerTvShowsBinding = itemView

        fun bindTVShow(tvShow: TVShows) {
            binding.tvShow = tvShow
            binding.executePendingBindings()
        }
    }

    interface TVShowsListener {
        fun onTVShowClicked(tvShows: TVShows)
    }
}
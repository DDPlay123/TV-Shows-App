package com.tutorial.tvshowsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.databinding.ItemContainerTvShowsBinding

class WatchListAdapter(private val tvShows: MutableList<TVShows>, private val watchListListener: WatchListListener)
    : RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WatchListViewHolder {
        return WatchListViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_container_tv_shows,
                parent, false) as ItemContainerTvShowsBinding
        )
    }

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        holder.bindTVShow(tvShows[position])
        holder.binding.root.setOnClickListener {
            watchListListener.onTVShowClicked(tvShows[position])
        }
        holder.binding.imageDelete.setOnClickListener {
            watchListListener.removeTVShowFromWatchList(tvShows[position], position)
        }
    }

    override fun getItemCount(): Int = tvShows.size

    class WatchListViewHolder(itemView: ItemContainerTvShowsBinding)
        : RecyclerView.ViewHolder(itemView.root) {

        val binding: ItemContainerTvShowsBinding = itemView

        fun bindTVShow(tvShow: TVShows) {
            binding.tvShow = tvShow
            binding.imageDelete.visibility = View.VISIBLE
            binding.executePendingBindings() // 即時更新
        }
    }

    interface WatchListListener {
        fun onTVShowClicked(tvShows: TVShows)
        fun removeTVShowFromWatchList(tvShow: TVShows, position: Int)
    }
}
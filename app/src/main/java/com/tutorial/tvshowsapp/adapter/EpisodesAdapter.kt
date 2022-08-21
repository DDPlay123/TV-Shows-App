package com.tutorial.tvshowsapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.databinding.ItemContainerEpisodeBinding
import com.tutorial.tvshowsapp.models.showDetails.Episodes

class EpisodesAdapter(private val episodes: MutableList<Episodes>):
    RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        return EpisodeViewHolder(
            DataBindingUtil.inflate<ViewDataBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_container_episode,
                parent, false) as ItemContainerEpisodeBinding
        )
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        holder.bindEpisode(episodes[position])
    }

    override fun getItemCount(): Int = episodes.size

    class EpisodeViewHolder(itemView: ItemContainerEpisodeBinding)
        : RecyclerView.ViewHolder(itemView.root) {

        private val binding: ItemContainerEpisodeBinding = itemView

        fun bindEpisode(episode: Episodes) {
            binding.run {
                val title: String
                val season: String = episode.season
                var episodeNumber: String = episode.episode

                if (episodeNumber.length == 1)
                   episodeNumber = "0$episodeNumber"

                title = "第 $season 季 | 第 $episodeNumber 集"

                setTitle(title)
                name = episode.name
                airDate = episode.airDate
            }
        }
    }
}
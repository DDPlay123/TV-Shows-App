package com.tutorial.tvshowsapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tutorial.tvshowsapp.Adapter.TVShowsAdapter
import com.tutorial.tvshowsapp.models.TVShows
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.ViewModel.MostPopularTVShowsViewModel
import com.tutorial.tvshowsapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: MostPopularTVShowsViewModel

    private lateinit var tvShowsAdapter: TVShowsAdapter
    private var tvShows: MutableList<TVShows> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        doInitialization()
    }

    private fun doInitialization() {
        // 連接 ViewModel
        viewModel = ViewModelProvider(this)[MostPopularTVShowsViewModel::class.java]
        // 初始化 RecyclerView
        tvShowsAdapter = TVShowsAdapter(tvShows)
        activityMainBinding.rvTvShows.visibility = View.VISIBLE
        activityMainBinding.rvTvShows.setHasFixedSize(true)
        activityMainBinding.rvTvShows.adapter = tvShowsAdapter
        // 取得資料
        getMostPopularTVShows()
    }

    private fun getMostPopularTVShows() {
        activityMainBinding.isLoading = true // <-- 指向 XML 的變數
        viewModel.getMostPopularTVShows(0).observe(this) { res ->
            activityMainBinding.isLoading = false
            if (res != null)
                tvShows.addAll(res.tvShows)
                tvShowsAdapter.notifyDataSetChanged()
        }
    }
}
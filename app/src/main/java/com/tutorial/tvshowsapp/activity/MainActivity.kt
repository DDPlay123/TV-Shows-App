package com.tutorial.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.adapter.TVShowsAdapter
import com.tutorial.tvshowsapp.viewModel.MostPopularTVShowsViewModel
import com.tutorial.tvshowsapp.databinding.ActivityMainBinding
import com.tutorial.tvshowsapp.manager.ToastManager

class MainActivity : BaseActivity(), TVShowsAdapter.TVShowsListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var viewModel: MostPopularTVShowsViewModel

    private lateinit var tvShowsAdapter: TVShowsAdapter
    private var tvShows: MutableList<TVShows> = ArrayList()
    private var currentPage: Int = 1
    private var totalAvailablePages = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        doInitialization()
    }

    override fun onTVShowClicked(tvShows: TVShows) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShows", tvShows)
        startActivity(intent)
    }

    private fun doInitialization() {
        // 連接 ViewModel
        viewModel = ViewModelProvider(this)[MostPopularTVShowsViewModel::class.java]
        // 初始化 RecyclerView
        tvShowsAdapter = TVShowsAdapter(tvShows, this)
        activityMainBinding.rvTvShows.visibility = View.VISIBLE
        activityMainBinding.rvTvShows.setHasFixedSize(true)
        activityMainBinding.rvTvShows.adapter = tvShowsAdapter
        activityMainBinding.rvTvShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            // 當頁面到底時，可以自動抓 API
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (!activityMainBinding.rvTvShows.canScrollVertically(1))
                    if (currentPage <= totalAvailablePages) {
                        currentPage += 1
                        getMostPopularTVShows()
                    } else
                        ToastManager.instance.showToast(this@MainActivity, "沒有更多資料了...", true)
            }
        })
        // 取得資料
        getMostPopularTVShows()
    }

    private fun getMostPopularTVShows() {
        ToastManager.instance.showToast(this, "載入中...", true)
        toggleLoading() // <-- 顯示 loading view
        viewModel.getMostPopularTVShows(currentPage).observe(this) { res ->
            toggleLoading() // <-- 停止 loading view
            if (res != null) {
                ToastManager.instance.cancelToast()
                val oldCount = tvShows.size
                tvShows.addAll(res.tvShows)
                totalAvailablePages = res.totalPages
                tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size)
            }
        }
    }

    private fun toggleLoading() {
        // 控制 loading
        if (currentPage == 1)
            activityMainBinding.isLoading =
                !(activityMainBinding.isLoading != null && activityMainBinding.isLoading == true)
        else
            activityMainBinding.isLoadingMore =
                !(activityMainBinding.isLoadingMore != null && activityMainBinding.isLoadingMore == true)
    }
}
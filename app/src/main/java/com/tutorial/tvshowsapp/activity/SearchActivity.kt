package com.tutorial.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.adapter.TVShowsAdapter
import com.tutorial.tvshowsapp.databinding.ActivitySearchBinding
import com.tutorial.tvshowsapp.manager.ToastManager
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.viewModel.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList

class SearchActivity : BaseActivity(), TVShowsAdapter.TVShowsListener {
    private lateinit var activitySearchBinding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    private lateinit var tvShowsAdapter: TVShowsAdapter
    private var tvShows: MutableList<TVShows> = ArrayList()
    private var currentPage: Int = 1
    private var totalAvailablePages = 1

    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySearchBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)

        doInitialization()
    }

    override fun onTVShowClicked(tvShows: TVShows) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShows", tvShows)
        startActivity(intent)
    }

    private fun doInitialization() {
        activitySearchBinding.run {
            // 連接 ViewModel
            viewModel = ViewModelProvider(this@SearchActivity)[SearchViewModel::class.java]
            // 初始化 RecyclerView
            tvShowsAdapter = TVShowsAdapter(tvShows, this@SearchActivity)
            rvTvShows.visibility = View.VISIBLE
            rvTvShows.setHasFixedSize(true)
            rvTvShows.adapter = tvShowsAdapter
            rvTvShows.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                // 滾動時，隱藏鍵盤
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    hideKeyBoard(this@SearchActivity)
                }
                // 當頁面到底時，可以自動抓 API
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!rvTvShows.canScrollVertically(1))
                        if (edSearch.text.toString().isNotEmpty())
                            if (currentPage <= totalAvailablePages) {
                                currentPage += 1
                                searchTVShows(edSearch.text.toString())
                            } else
                                ToastManager.instance.showToast(this@SearchActivity, "沒有更多資料了...", true)
                }
            })
            setListener()
        }
    }

    private fun searchTVShows(query: String) {
        toggleLoading() // <-- 顯示 loading view
        viewModel.searchTVShow(query, currentPage).observe(this) { res ->
            toggleLoading() // <-- 停止 loading view
            if (res != null) {
                val oldCount = tvShows.size
                tvShows.addAll(res.tvShows)
                totalAvailablePages = res.totalPages
                tvShowsAdapter.notifyItemRangeInserted(oldCount, tvShows.size)
            }
        }
    }

    private fun setListener() {
        activitySearchBinding.run {
            // 返回
            layoutBack.setOnClickListener { onBackPressed() }
            // 搜尋
            imageSearch.setOnClickListener {
                edSearch.requestFocus()
                showKeyBoard(this@SearchActivity, edSearch)
            }
            edSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    timer?.cancel()
                }

                override fun afterTextChanged(editable: Editable?) {
                    if (editable.toString().trim().isNotEmpty()) {
                        timer = Timer()
                        timer?.schedule(object : TimerTask() {
                            override fun run() {
                                Handler(Looper.getMainLooper()).post {
                                    currentPage = 1
                                    totalAvailablePages = 1
                                    searchTVShows(editable.toString())
                                }
                            }
                        }, 500)
                    } else {
                        tvShows.clear()
                        tvShowsAdapter.notifyDataSetChanged()
                    }
                }
            })
            edSearch.requestFocus()
        }
    }

    private fun toggleLoading() {
        // 控制 loading
        if (currentPage == 1)
            activitySearchBinding.isLoading =
                !(activitySearchBinding.isLoading != null && activitySearchBinding.isLoading == true)
        else
            activitySearchBinding.isLoadingMore =
                !(activitySearchBinding.isLoadingMore != null && activitySearchBinding.isLoadingMore == true)
    }
}
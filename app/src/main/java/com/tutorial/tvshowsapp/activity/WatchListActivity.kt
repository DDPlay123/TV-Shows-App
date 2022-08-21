package com.tutorial.tvshowsapp.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.adapter.WatchListAdapter
import com.tutorial.tvshowsapp.databinding.ActivityWatchListBinding
import com.tutorial.tvshowsapp.manager.ToastManager
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.utilities.IS_WATCH_LIST_UPDATED
import com.tutorial.tvshowsapp.viewModel.WatchListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class WatchListActivity : BaseActivity(), WatchListAdapter.WatchListListener {
    private lateinit var activityWatchListBinding: ActivityWatchListBinding
    private lateinit var viewModel: WatchListViewModel

    private lateinit var watchListAdapter: WatchListAdapter
    private var watchList: MutableList<TVShows> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list)

        doInitialization()
    }

    override fun onResume() {
        super.onResume()
        if (IS_WATCH_LIST_UPDATED) {
            loadWatchList()
            IS_WATCH_LIST_UPDATED = false
        }
    }

    override fun onStop() {
        super.onStop()
        viewModel.closeDatabase()
    }

    // 點擊事件，跳頁
    override fun onTVShowClicked(tvShows: TVShows) {
        val intent = Intent(applicationContext, TVShowDetailsActivity::class.java)
        intent.putExtra("tvShows", tvShows)
        startActivity(intent)
    }

    // 刪除項目
    override fun removeTVShowFromWatchList(tvShow: TVShows, position: Int) {
        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(viewModel.removeTVShowFromWatchList(tvShow)
            .subscribeOn(Schedulers.computation()) // Background Thread
            .observeOn(AndroidSchedulers.mainThread()) // Main Thread
            .subscribe {
                watchList.removeAt(position)
                watchListAdapter.notifyItemRemoved(position)
                watchListAdapter.notifyItemRangeChanged(position, watchListAdapter.itemCount)

                ToastManager.instance.showToast(this, "共有${watchList.size}筆", true)

                compositeDisposable.dispose() // 解除訂閱
            }
        )
    }

    private fun loadWatchList() {
        ToastManager.instance.showToast(this, "載入中...", true)
        toggleLoading()

        val compositeDisposable = CompositeDisposable()

        compositeDisposable.add(viewModel.loadWatchList()
            .observeOn(AndroidSchedulers.mainThread()) // Main Thread
            .subscribe { tvShows ->
                toggleLoading()
                ToastManager.instance.showToast(this, "共有${tvShows.size}筆", true)

                if (watchList.size > 0)
                    watchList.clear() // 先清空，避免重疊

                watchList.addAll(tvShows)
                watchListAdapter.notifyDataSetChanged()

                compositeDisposable.dispose() // 解除訂閱
            }
        )
    }

    private fun doInitialization() {
        // 連接 ViewModel
        viewModel = ViewModelProvider(this)[WatchListViewModel::class.java]
        // 初始化 RecyclerView
        watchListAdapter = WatchListAdapter(watchList, this)
        activityWatchListBinding.rvWatchList.visibility = View.VISIBLE
        activityWatchListBinding.rvWatchList.setHasFixedSize(true)
        activityWatchListBinding.rvWatchList.adapter = watchListAdapter
        setListener()
        loadWatchList()
    }

    private fun setListener() {
        activityWatchListBinding.layoutBack.setOnClickListener { onBackPressed() }
    }

    private fun toggleLoading() {
        // 控制 loading
        activityWatchListBinding.isLoading =
            !(activityWatchListBinding.isLoading != null && activityWatchListBinding.isLoading == true)
    }
}
package com.tutorial.tvshowsapp.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.databinding.ActivityWatchListBinding
import com.tutorial.tvshowsapp.manager.ToastManager
import com.tutorial.tvshowsapp.viewModel.WatchListViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable

class WatchListActivity : BaseActivity() {
    private lateinit var activityWatchListBinding: ActivityWatchListBinding
    private lateinit var viewModel: WatchListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityWatchListBinding = DataBindingUtil.setContentView(this, R.layout.activity_watch_list)

        doInitialization()
    }

    override fun onResume() {
        super.onResume()
        loadWatchList()
    }

    private fun loadWatchList() {
        ToastManager.instance.showToast(this, "載入中...", true)
        toggleLoading()
        CompositeDisposable().add(viewModel.loadWatchList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { tvShows ->
                toggleLoading()
                ToastManager.instance.showToast(this, "共有${tvShows.size}筆", true)
            }
        )
    }

    private fun doInitialization() {
        // 連接 ViewModel
        viewModel = ViewModelProvider(this)[WatchListViewModel::class.java]
        setListener()
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
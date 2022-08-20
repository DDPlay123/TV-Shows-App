package com.tutorial.tvshowsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.databinding.ActivityTvshowDetailsBinding
import com.tutorial.tvshowsapp.manager.ToastManager
import com.tutorial.tvshowsapp.viewModel.TVShowDetailsViewModel

class TVShowDetailsActivity : AppCompatActivity() {
    private lateinit var activityTVShowDetailsBinding: ActivityTvshowDetailsBinding
    private lateinit var viewModel: TVShowDetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTVShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details)

        doInitialization()
    }

    private fun doInitialization() {
        // 連接 ViewModel
        viewModel = ViewModelProvider(this)[TVShowDetailsViewModel::class.java]

        getTVShowDetails()
    }

    private fun getTVShowDetails() {
        ToastManager.instance.showToast(this, "載入中...", true)
        toggleLoading()
        val tvShowId: String = intent.getIntExtra("id", -1).toString()
        viewModel.getTVShowDetails(tvShowId).observe(this) { res ->
            toggleLoading()
            if (res != null) {

            }
        }
    }

    private fun toggleLoading() {
        // 控制 loading
        activityTVShowDetailsBinding.isLoading =
            !(activityTVShowDetailsBinding.isLoading != null && activityTVShowDetailsBinding.isLoading == true)
    }
}
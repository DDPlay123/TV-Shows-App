package com.tutorial.tvshowsapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.adapter.ImageSliderAdapter
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
                ToastManager.instance.cancelToast()
                loadImageSlider(res.tvShowDetails.pictures)
            }
        }
    }

    private fun toggleLoading() {
        // 控制 loading
        activityTVShowDetailsBinding.isLoading =
            !(activityTVShowDetailsBinding.isLoading != null && activityTVShowDetailsBinding.isLoading == true)
    }

    private fun loadImageSlider(sliderImages: MutableList<String>) {
        activityTVShowDetailsBinding.run {
            vpSlider.offscreenPageLimit = 1
            vpSlider.adapter = ImageSliderAdapter(sliderImages)
            vpSlider.visibility = View.VISIBLE
            viewFadingEdge.visibility = View.VISIBLE
            setupSliderIndicators(sliderImages.size)
            vpSlider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    setCurrentSliderIndicator(position)
                }
            })
        }
    }

    private fun setupSliderIndicators(count: Int) {
        val indicators: Array<AppCompatImageView?> = arrayOfNulls(count)
        Log.e("TAG", indicators.size.toString())
        val layoutParams: LinearLayoutCompat.LayoutParams = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 0, 10, 0)

        indicators.forEachIndexed { id, _ ->
            Log.e("TAG", "id")
            indicators[id] = AppCompatImageView(applicationContext)
            indicators[id]?.setImageDrawable(ContextCompat.getDrawable(
                applicationContext, R.drawable.background_slider_indicator_inactive
            ))

            indicators[id]?.layoutParams = layoutParams
            activityTVShowDetailsBinding.layoutSliderIndicators.addView(indicators[id])
        }

        activityTVShowDetailsBinding.layoutSliderIndicators.visibility = View.VISIBLE
        setCurrentSliderIndicator(0)
    }

    private fun setCurrentSliderIndicator(position: Int) {
        val childCount: Int = activityTVShowDetailsBinding.layoutSliderIndicators.childCount

        for (i in 0 until childCount) {
            val imageView: AppCompatImageView =
                activityTVShowDetailsBinding.layoutSliderIndicators.getChildAt(i) as AppCompatImageView

            if (i == position)
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.background_slider_indicator_active)
                )
            else
                imageView.setImageDrawable(
                    ContextCompat.getDrawable(applicationContext, R.drawable.background_slider_indicator_inactive)
                )
        }
    }
}
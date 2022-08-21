package com.tutorial.tvshowsapp.activity

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.adapter.ImageSliderAdapter
import com.tutorial.tvshowsapp.databinding.ActivityTvshowDetailsBinding
import com.tutorial.tvshowsapp.manager.ToastManager
import com.tutorial.tvshowsapp.models.showDetails.TVShowDetailsResponse
import com.tutorial.tvshowsapp.viewModel.TVShowDetailsViewModel
import java.util.*

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
        setListener()
        getTVShowDetails()
    }

    private fun setListener() {
        activityTVShowDetailsBinding.run {
            layoutBack.setOnClickListener { onBackPressed() }
        }
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
                setupDetails(res)
            }
        }
    }

    private fun toggleLoading() {
        // 控制 loading
        activityTVShowDetailsBinding.isLoading =
            !(activityTVShowDetailsBinding.isLoading != null && activityTVShowDetailsBinding.isLoading == true)
    }

    private fun setupDetails(res: TVShowDetailsResponse) {
        activityTVShowDetailsBinding.run {
            // 圖片資源
            tvShowImageURL = res.tvShowDetails.imagePath
            imageTVShow.visibility = View.VISIBLE
            // 基本資訊
            loadBasicTVShowDetails()
            // 描述訊息
            description = HtmlCompat.fromHtml(
                res.tvShowDetails.description,
                HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            // 開啟或收起 描述訊息
            tvReadMore.setOnClickListener {
                if (tvReadMore.text.toString() == getString(R.string.read_more)) {
                    tvDescription.maxLines = Int.MAX_VALUE
                    tvDescription.ellipsize = null
                    tvReadMore.text = getString(R.string.read_less)
                } else {
                    tvDescription.maxLines = 4
                    tvDescription.ellipsize = TextUtils.TruncateAt.END
                    tvReadMore.text = getString(R.string.read_more)
                }
            }
            // 其餘項目
            genre = if (res.tvShowDetails.genres != null)
                res.tvShowDetails.genres!![0]
            else
                "N/A"
            rating = String.format("%.2f", res.tvShowDetails.rating.toDouble())
            runtime = "${res.tvShowDetails.runtime} Min"
            viewDivider1.visibility = View.VISIBLE // 如有資料，再顯示
            viewDivider2.visibility = View.VISIBLE
            layoutMisc.visibility = View.VISIBLE
            // 按鈕功能
            btnWebSite.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(res.tvShowDetails.url)
                startActivity(intent)
            }
            btnWebSite.visibility = View.VISIBLE
            btnEpisodes.visibility = View.VISIBLE
        }
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

    private fun loadBasicTVShowDetails() {
        activityTVShowDetailsBinding.run {
            intent.extras.let {
                tvShowName = intent.getStringExtra("name") ?: ""
                networkCountry = "${intent.getStringExtra("network") ?: ""} (${intent.getStringExtra("country") ?: ""})"
                status = intent.getStringExtra("status") ?: ""
                startedDate = "Started on：${intent.getStringExtra("startDate") ?: ""}"
            }
        }
    }

    private fun setupSliderIndicators(count: Int) {
        val indicators: Array<AppCompatImageView?> = arrayOfNulls(count)
        val layoutParams: LinearLayoutCompat.LayoutParams = LinearLayoutCompat.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 0, 10, 0)

        indicators.forEachIndexed { id, _ ->
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
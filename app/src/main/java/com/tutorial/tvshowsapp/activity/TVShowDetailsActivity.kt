package com.tutorial.tvshowsapp.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.adapter.EpisodesAdapter
import com.tutorial.tvshowsapp.adapter.ImageSliderAdapter
import com.tutorial.tvshowsapp.databinding.ActivityTvshowDetailsBinding
import com.tutorial.tvshowsapp.databinding.DialogEpisodesBottomSheetBinding
import com.tutorial.tvshowsapp.manager.ToastManager
import com.tutorial.tvshowsapp.models.showDetails.TVShowDetailsResponse
import com.tutorial.tvshowsapp.models.tvShows.TVShows
import com.tutorial.tvshowsapp.viewModel.TVShowDetailsViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class TVShowDetailsActivity : BaseActivity() {
    private lateinit var activityTVShowDetailsBinding: ActivityTvshowDetailsBinding
    private lateinit var viewModel: TVShowDetailsViewModel

    private lateinit var tvShows: TVShows

    private lateinit var episodesBottomSheetDialog: BottomSheetDialog
    private lateinit var layoutEpisodeBinding: DialogEpisodesBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityTVShowDetailsBinding = DataBindingUtil.setContentView(this, R.layout.activity_tvshow_details)

        doInitialization()
    }

    private fun doInitialization() {
        // 連接 ViewModel
        viewModel = ViewModelProvider(this)[TVShowDetailsViewModel::class.java]
        setListener()
        tvShows = intent.getSerializableExtra("tvShows") as TVShows
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
        val tvShowId: String = tvShows.id.toString()
        viewModel.getTVShowDetails(tvShowId).observe(this) { res ->
            toggleLoading()
            if (res != null) {
                ToastManager.instance.cancelToast()
                loadImageSlider(res.tvShowDetails.pictures)
                setupDetails(res)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupDetails(res: TVShowDetailsResponse) {
        activityTVShowDetailsBinding.run {
            // 圖片資源
            tvShowImageURL = res.tvShowDetails.imagePath
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
            runtime = "${res.tvShowDetails.runtime} 分鐘"

            viewDivider1.visibility = View.VISIBLE // 如有資料，再顯示
            viewDivider2.visibility = View.VISIBLE
            layoutMisc.visibility = View.VISIBLE
            btnWebSite.visibility = View.VISIBLE
            btnEpisodes.visibility = View.VISIBLE
            imageWatchList.visibility = View.VISIBLE

            // 前往網頁
            btnWebSite.setOnClickListener {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(res.tvShowDetails.url)
                startActivity(intent)
            }

            // 查看播放日期
            btnEpisodes.setOnClickListener {
                episodesBottomSheetDialog = BottomSheetDialog(this@TVShowDetailsActivity, R.style.BottomSheetDialogTheme)
                layoutEpisodeBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(this@TVShowDetailsActivity),
                    R.layout.dialog_episodes_bottom_sheet,
                    findViewById(R.id.episodesContainer), false
                )

                episodesBottomSheetDialog.setContentView(layoutEpisodeBinding.root)
                layoutEpisodeBinding.rvEpisodes.adapter = EpisodesAdapter(res.tvShowDetails.episodes)
                layoutEpisodeBinding.tvTitle.text = "影集 | ${tvShows.name}"
                layoutEpisodeBinding.imageClose.setOnClickListener {
                    episodesBottomSheetDialog.dismiss()
                }

                // 預設完全展開用
//                val frameLayout: FrameLayout? = episodesBottomSheetDialog.findViewById(
//                    com.google.android.material.R.id.design_bottom_sheet
//                )
//                if (frameLayout != null) {
//                    val bottomSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(frameLayout)
//                    bottomSheetBehavior.peekHeight = Resources.getSystem().displayMetrics.heightPixels
//                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//                }

                episodesBottomSheetDialog.show()
            }

            // 添加至觀看清單
            imageWatchList.setOnClickListener {
                CompositeDisposable().add(viewModel.addToWatchList(tvShows)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        imageWatchList.setImageResource(R.drawable.ic_check_24)
                        ToastManager.instance.showToast(this@TVShowDetailsActivity, "添加成功", true)
                    }
                )
            }
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
            tvShowName = tvShows.name
            networkCountry = "${tvShows.network} (${tvShows.country})"
            status = tvShows.status
            startedDate = "Started on：${tvShows.startDate}"
        }
    }

    private fun toggleLoading() {
        // 控制 loading
        activityTVShowDetailsBinding.isLoading =
            !(activityTVShowDetailsBinding.isLoading != null && activityTVShowDetailsBinding.isLoading == true)
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
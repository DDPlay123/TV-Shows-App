package com.tutorial.tvshowsapp.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tutorial.tvshowsapp.R
import com.tutorial.tvshowsapp.ViewModel.MostPopularTVShowsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MostPopularTVShowsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[MostPopularTVShowsViewModel::class.java]

        getMostPopularTVShows()
    }

    private fun getMostPopularTVShows() {
        viewModel.getMostPopularTVShows(0).observe(this) {
            Toast.makeText(applicationContext, "Total Pages：${it.totalPages}", Toast.LENGTH_SHORT)
                .show()
        }
    }
}
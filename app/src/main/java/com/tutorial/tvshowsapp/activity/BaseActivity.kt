package com.tutorial.tvshowsapp.activity

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.tutorial.tvshowsapp.room.TVShowsDatabase
import com.tutorial.tvshowsapp.utilities.TVShows_Database

abstract class BaseActivity: AppCompatActivity() {

    private var tvShowsDatabase: TVShowsDatabase? = null

    fun initTVShowDatabase(context: Context): TVShowsDatabase? {
        tvShowsDatabase = Room.databaseBuilder(context, TVShowsDatabase::class.java, TVShows_Database)
            .fallbackToDestructiveMigration()
            .build()

        return tvShowsDatabase
    }

    private fun closeDatabase() {
        tvShowsDatabase?.close()
        tvShowsDatabase = null
    }

    fun showKeyBoard(activity: AppCompatActivity, ed: EditText){
        val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(ed, 0)
    }

    fun hideKeyBoard(activity: AppCompatActivity) {
        activity.currentFocus?.let {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(it.windowToken,0)
        }
    }

    fun hideKeyBoard(context: Context, view: View) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken,0)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if (level <= TRIM_MEMORY_BACKGROUND)
            System.gc()
    }

    override fun onStop() {
        super.onStop()
//        closeDatabase()
    }

    override fun onDestroy() {
        super.onDestroy()
//        closeDatabase()
    }
}
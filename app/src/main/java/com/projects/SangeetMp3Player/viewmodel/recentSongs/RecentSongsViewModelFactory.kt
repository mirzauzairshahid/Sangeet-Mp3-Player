package com.projects.SangeetMp3Player.viewmodel.recentSongs

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class RecentSongsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecentSongsViewModel::class.java)) {
            return RecentSongsViewModel(
                application
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


package com.projects.SangeetMp3Player.viewmodel.recentSongs

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.projects.SangeetMp3Player.database.recentSongs.RecentSongEntity
import com.projects.SangeetMp3Player.repository.RecentSongsRepository
import kotlinx.coroutines.launch

class RecentSongsViewModel(application: Application) : ViewModel() {
    private val mRecentSongsRepository: RecentSongsRepository = RecentSongsRepository(application)
    val recentSongs: LiveData<List<RecentSongEntity>>
        get() = mRecentSongsRepository.mRecentSongs


    fun deleteRecentSong(song: RecentSongEntity) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mRecentSongsRepository.deleteSong(song)
        }
    }

    fun updateRecentSong(song: RecentSongEntity) {
        //use of coroutine scope from viewModelScope
        viewModelScope.launch {
            mRecentSongsRepository.insertSong(song)
        }
    }
}
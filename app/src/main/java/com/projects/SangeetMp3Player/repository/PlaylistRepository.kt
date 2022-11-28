package com.projects.SangeetMp3Player.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.projects.SangeetMp3Player.database.playlists.PlaylistConverter
import com.projects.SangeetMp3Player.database.playlists.PlaylistDao
import com.projects.SangeetMp3Player.database.playlists.PlaylistEntity
import com.projects.SangeetMp3Player.database.playlists.PlaylistRoomDatabase

class PlaylistRepository(application: Application) {

    private var mPlaylistDao: PlaylistDao

    val db: PlaylistRoomDatabase

    init {
        db = PlaylistRoomDatabase.getPlaylistDatabase(application)
        mPlaylistDao = db.playlistDao()
    }


    suspend fun createPlaylist(playlistEntity: PlaylistEntity) =
        mPlaylistDao.createPlaylist(playlistEntity)


    suspend fun deletePlaylist(playlistEntity: PlaylistEntity) =
        mPlaylistDao.deletePlaylist(playlistEntity)


    val allPlaylist: LiveData<List<PlaylistEntity>>
        get() {
            return mPlaylistDao.allPlaylist
        }

    suspend fun getPlaylistSongsById(id: Int): String? = mPlaylistDao.getPlaylistSongsById(id)
    suspend fun getAllPlaylistsId(): List<Int> = mPlaylistDao.getAllPlaylistsId()

    fun getPlaylistSongsByIdLive(id: Int): LiveData<String> = mPlaylistDao.getPlaylistSongsByIdLive(id)





    suspend fun updatePlaylist(id: Int, mSongs: List<Int>) {
        mPlaylistDao.updatePlaylist(id, PlaylistConverter.fromList(mSongs)!!)
    }
}
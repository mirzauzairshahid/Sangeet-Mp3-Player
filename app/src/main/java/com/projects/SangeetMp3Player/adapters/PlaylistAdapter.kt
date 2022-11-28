package com.projects.SangeetMp3Player.adapters

import android.content.Context
import android.util.Log
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projects.SangeetMp3Player.R
import com.projects.SangeetMp3Player.database.playlists.PlaylistEntity

class PlaylistAdapter(context: Context) :
    RecyclerView.Adapter<PlaylistAdapter.AllPlaylistViewHolder>() {

    private var entity: PlaylistEntity? = null


    private fun setSelectedPlaylist(p: PlaylistEntity) {
        entity = p
    }

    fun getSelectedPlaylist(): PlaylistEntity? = entity

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    private var list: List<PlaylistEntity>? = null
    var onPlaylistClickCallback: ((playlist: PlaylistEntity) -> Unit)? = null

    class AllPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        val playlistName: TextView = view.findViewById(R.id.playlistName)
        val playlistCardView: LinearLayout = view.findViewById(R.id.PlaylistsCardView)

        init {
            view.setOnCreateContextMenuListener(this)
        }


        override fun onCreateContextMenu(
            menu: ContextMenu?,
            v: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            menu!!.add(0, R.id.ctx_remove_playlist, 0, "Remove  Playlist")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllPlaylistViewHolder {
        val playlistItemView: View = mInflater.inflate(R.layout.single_playlist_item, parent, false)
        return AllPlaylistViewHolder(
            playlistItemView
        )
    }

    override fun onBindViewHolder(holder: AllPlaylistViewHolder, position: Int) {
        val currentPlaylist: PlaylistEntity = list!![position]
        holder.playlistName.text = currentPlaylist.name

        holder.playlistCardView.setOnLongClickListener {
            setSelectedPlaylist(currentPlaylist)
            Log.i("SELECTEDPLAYLIST",entity.toString())
            false
        }

        holder.playlistCardView.setOnClickListener {
            onPlaylistClickCallback?.invoke(currentPlaylist)
        }


    }

    fun setPlayLists(mplaylists: List<PlaylistEntity>) {
        list = mplaylists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (list != null)
            list!!.size;
        else 0;
    }

}

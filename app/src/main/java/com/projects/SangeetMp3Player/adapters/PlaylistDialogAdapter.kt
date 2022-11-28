package com.projects.SangeetMp3Player.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.projects.SangeetMp3Player.R
import com.projects.SangeetMp3Player.database.playlists.PlaylistEntity

class PlaylistDialogAdapter(context: Context) :
    RecyclerView.Adapter<PlaylistDialogAdapter.AllPlaylistViewHolder>() {

    val mInflater: LayoutInflater = LayoutInflater.from(context)

    private var list: List<PlaylistEntity>? = null

    var playlistClickCallback: ((id: Int) -> Unit)? = null

    class AllPlaylistViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playlistName: TextView = view.findViewById(R.id.playlistName)
        val playlistItem: LinearLayout = view.findViewById(R.id.PlaylistsCardView)
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
        holder.playlistItem.setOnClickListener {
            playlistClickCallback?.invoke(currentPlaylist.id)
        }
    }


    fun setPlayLists(mPlaylists: List<PlaylistEntity>) {
        list = mPlaylists
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (list != null)
            list!!.size;
        else 0;
    }

}

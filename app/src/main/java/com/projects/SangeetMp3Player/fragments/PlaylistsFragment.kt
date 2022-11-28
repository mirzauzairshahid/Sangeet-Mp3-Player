package com.projects.SangeetMp3Player.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.Toolbar
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.projects.SangeetMp3Player.adapters.PlaylistAdapter
import com.projects.SangeetMp3Player.R
import com.projects.SangeetMp3Player.database.playlists.PlaylistEntity
import com.projects.SangeetMp3Player.uicomponents.CustomDialog
import com.projects.SangeetMp3Player.viewmodel.playlists.PlaylistViewModel
import com.projects.SangeetMp3Player.viewmodel.playlists.PlaylistViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception


class PlaylistsFragment : Fragment() {

    lateinit var recyclerViewPlaylists: RecyclerView
    lateinit var recylcerViewPlaylistAdapter: PlaylistAdapter
    lateinit var toolbar: Toolbar
    lateinit var fabCreatePlaylist: FloatingActionButton
    lateinit var playlistInputDialog: CustomDialog
    lateinit var favCardView: CardView
    lateinit var emptyPlaylistLayout: RelativeLayout


    private lateinit var mPlaylistViewModel: PlaylistViewModel
    private lateinit var mPlaylistViewModelFactory: PlaylistViewModelFactory
    var selectedPlaylist: PlaylistEntity? = null

    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPlaylistViewModelFactory =
            PlaylistViewModelFactory(
                activity!!.application
            )
        mPlaylistViewModel =
            ViewModelProvider(this, mPlaylistViewModelFactory).get(PlaylistViewModel::class.java)

        mPlaylistViewModel.allPlaylists.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty())
                emptyPlaylistLayout.visibility = View.VISIBLE
            else
                emptyPlaylistLayout.visibility = View.GONE

            scope.launch {
                recylcerViewPlaylistAdapter.setPlayLists(it!!)
            }

        })

        recylcerViewPlaylistAdapter.onPlaylistClickCallback = fun(playlist: PlaylistEntity) {

            val bundle = Bundle()
            bundle.putInt("ID", playlist.id)
            bundle.putString("NAME", playlist.name)
            bundle.putString("SONGS", playlist.songs)
            activity!!.supportFragmentManager.beginTransaction()
                .add(R.id.frame, SinglePlaylistFragment::class.java, bundle)
                .commit()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_playlists, container, false)
        recyclerViewPlaylists = view.findViewById(R.id.recyclerViewPlaylists)
        toolbar = view.findViewById(R.id.toolbar)
        fabCreatePlaylist = view.findViewById(R.id.fabCreatePlaylist)
        playlistInputDialog = CustomDialog(activity as Context)
        favCardView = view.findViewById(R.id.favCardView)
        emptyPlaylistLayout = view.findViewById(R.id.emptyPlaylistLayout)
        toolbar.title = "Playlists"

        favCardView.setOnClickListener {
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.frame, FavFragment())
                .commit()
        }

        fabCreatePlaylist.setOnClickListener {
            playlistInputDialog.show()
        }

        playlistInputDialog.positiveButtonCallback = fun(playlistName: String) {
            if (playlistName.isNotBlank()) {
                mPlaylistViewModel.createPlaylist(
                    PlaylistEntity(
                        playlistName.hashCode(),
                        playlistName,
                        ""
                    )
                )
                Toast.makeText(
                    activity as Context,
                    "$playlistName playlist created ",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    activity as Context,
                    "Discarding Empty Playlist Name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        if (activity != null) {
            emptyPlaylistLayout.visibility = View.GONE
            recylcerViewPlaylistAdapter =
                PlaylistAdapter(
                    activity as Context
                )
            recyclerViewPlaylists.adapter = recylcerViewPlaylistAdapter
            recyclerViewPlaylists.layoutManager = LinearLayoutManager(activity)
            recyclerViewPlaylists.addItemDecoration(
                DividerItemDecoration(
                    recyclerViewPlaylists.context,
                    (recyclerViewPlaylists.layoutManager as LinearLayoutManager).orientation
                )
            )

        }

        return view
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {

        try {
            selectedPlaylist = recylcerViewPlaylistAdapter.getSelectedPlaylist()
            Log.e("REMOVEPLAYLIST", selectedPlaylist!!.toString())
        } catch (e: Exception) {
            Log.e("REMOVEPLAYLIST", e.message.toString())
            return super.onContextItemSelected(item)
        }
        when (item.itemId) {
            R.id.ctx_remove_playlist -> {
                scope.launch {
                    if (selectedPlaylist != null)
                        mPlaylistViewModel.deletePlaylist(selectedPlaylist!!)
                    else
                        Toast.makeText(
                            activity as Context,
                            "Failed to delete Playlist ",
                            Toast.LENGTH_SHORT
                        ).show()
                }
            }
            else -> {
                Toast.makeText(activity as Context, "No Playlist Selected", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return super.onContextItemSelected(item)
    }
}


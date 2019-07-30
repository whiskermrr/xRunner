package com.whisker.mrr.xrunner.presentation.adapters

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.xrunner.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.album_row_layout.view.*
import org.jetbrains.anko.layoutInflater
import java.io.File

class AlbumsAdapter : RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {

    private var albums: List<Album> = listOf()
    private val clickSubject: PublishSubject<Album> = PublishSubject.create()

    fun setAlbums(newAlbums: List<Album>) {
        albums = newAlbums
        notifyDataSetChanged()
    }

    fun clickEvent() : Observable<Album> {
        return clickSubject
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.album_row_layout, parent, false)
        return AlbumViewHolder(view)
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.bind(albums[position])
    }

    inner class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(album: Album) {
            itemView.tvAlbumTitle.text = album.title
            itemView.tvAlbumArtist.text = album.artist

            if(!album.albumArt.isNullOrEmpty()) {
                Picasso.get().load(File(album.albumArt))
                        .into(itemView.ivAlbumArt)
            } else {
                Picasso.get().load(R.drawable.no_artwork)
                        .into(itemView.ivAlbumArt)
            }

            itemView.setOnClickListener { clickSubject.onNext(album) }
        }
    }
}
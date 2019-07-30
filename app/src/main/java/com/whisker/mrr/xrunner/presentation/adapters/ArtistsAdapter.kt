package com.whisker.mrr.xrunner.presentation.adapters

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.whisker.mrr.domain.model.Artist
import com.whisker.mrr.xrunner.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.song_row_layout.view.*
import org.jetbrains.anko.layoutInflater
import java.io.File
import java.lang.StringBuilder

class ArtistsAdapter : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    private var artists: List<Artist> = mutableListOf()
    private val clickSubject: PublishSubject<Artist> = PublishSubject.create()

    fun setArtists(newArtists: List<Artist>) {
        artists = newArtists
        notifyDataSetChanged()
    }

    fun clickEvent() : Observable<Artist> {
        return clickSubject
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.song_row_layout, parent, false)
        return ArtistViewHolder(view)
    }

    override fun getItemCount(): Int {
        return artists.size
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        holder.bind(artists[position])
    }

    inner class ArtistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(artist: Artist) {
            itemView.tvMusicTitle.text = artist.artistName
            itemView.tvMusicDescription.text = getArtistDescription(artist)

            val coverArt = artist.albums.find { it.albumArt != null }?.albumArt
            if(coverArt != null) {
                Picasso.get().load(File(coverArt))
                    .into(itemView.ivMusicImage)
            } else {
                Picasso.get().load(R.drawable.no_artwork)
                    .into(itemView.ivMusicImage)
            }
            itemView.setOnClickListener { clickSubject.onNext(artist) }
        }
    }

    private fun getArtistDescription(artist: Artist) : String {
        val builder = StringBuilder()
        builder.append(artist.albums.size.toString())
        builder.append(if(artist.albums.size == 1) " Album" else " Albums")
        builder.append(", ").append(artist.numberOfTracks)
        builder.append(if(artist.numberOfTracks == 1) " Song" else " Songs")
        return builder.toString()
    }
}
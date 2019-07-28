package com.whisker.mrr.xrunner.presentation.adapters

import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.xrunner.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.song_row_layout.view.*
import org.jetbrains.anko.layoutInflater

class SongsAdapter : RecyclerView.Adapter<SongsAdapter.SongViewHolder>() {

    private var songs: List<Song> = mutableListOf()
    private val metaReceiver = MediaMetadataRetriever()
    private val clickSubject: PublishSubject<Int> = PublishSubject.create()

    fun setSongs(newSongs: List<Song>) {
        songs = newSongs
        notifyDataSetChanged()
    }

    fun getSongs() : List<Song> {
        return songs
    }

    fun clickEvent() : Observable<Int> {
        return clickSubject
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = parent.context.layoutInflater.inflate(R.layout.song_row_layout, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        holder.bind(songs[position])
    }

    inner class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(song: Song) {
            itemView.tvSongTitle.text = song.title
            itemView.tvSongArtist.text = song.artist
            metaReceiver.setDataSource(song.data)

            val coverArt = metaReceiver.embeddedPicture
            if(coverArt != null) {
                val options = BitmapFactory.Options()
                options.inSampleSize = 2
                itemView.ivSongImage
                    .setImageBitmap(BitmapFactory.decodeByteArray(coverArt, 0, coverArt.size, options))
            } else {
                Picasso.get().load(R.drawable.no_artwork)
                    .into(itemView.ivSongImage)
            }
            itemView.setOnClickListener { clickSubject.onNext(adapterPosition) }
        }
    }
}
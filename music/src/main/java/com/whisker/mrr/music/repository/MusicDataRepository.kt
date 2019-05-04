package com.whisker.mrr.music.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.repository.MusicRepository
import io.reactivex.Single

class MusicDataRepository(private val context: Context) : MusicRepository {

    override fun getSongs(albumID: Long?): Single<List<Song>> {
        val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )

        var selection: String? = null
        var selectionArgs: Array<String>? = null

        albumID?.let { id ->
            selection = MediaStore.Audio.Media.ALBUM_ID + "= ?"
            selectionArgs = arrayOf(id.toString())
        } ?: run {
            selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " +
                    MediaStore.Audio.Media.DISPLAY_NAME + " LIKE '%.mp3'"
        }

        val musicCursor: Cursor? = context.contentResolver.query(
            externalUri, projection, selection, selectionArgs, null)

        val songsList: MutableList<Song> = mutableListOf()

        musicCursor?.let { cursor ->
            while(cursor.moveToNext()) {
                songsList.add(
                    Song(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getLong(5),
                        cursor.getLong(6)
                    )
                )
            }
        }

        musicCursor?.close()
        return Single.just(songsList)
    }

    override fun getAlbums(): Single<List<Album>> {
        val externalUri: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

        val projection: Array<String> = arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.ALBUM_ART
        )

        val albumCursor = context.contentResolver.query(
            externalUri, projection, null, null, null)

        val albumsList: MutableList<Album> = mutableListOf()

        albumCursor?.let { cursor ->
            while(cursor.moveToNext()) {
                albumsList.add(
                    Album(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                    )
                )
            }
        }

        albumCursor?.close()
        return Single.just(albumsList)
    }

    override fun getLastPlaylist(): Single<List<Song>> {
        return Single.just(listOf())
    }
}
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

    companion object {
        const val MUSIC_PREFERENCES = "music_preferences"
        const val KEY_LAST_ALBUM_ID = "last_album_id"
    }

    private val externalUri: Uri by lazy { MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI }

    private val songsProjections: Array<String> by lazy {
        arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.ALBUM_ID
        )
    }

    private val albumProjections: Array<String> by lazy {
        arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.ALBUM_ART
        )
    }

    override fun getSongs(albumID: Long?): Single<List<Song>> {
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
            externalUri, songsProjections, selection, selectionArgs, null)

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
        albumID?.let {
            saveLastAlbumID(it)
        }
        return Single.just(songsList)
    }

    private fun saveLastAlbumID(albumID: Long) {
        val sharedPreferences = context.getSharedPreferences(MUSIC_PREFERENCES, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putLong(KEY_LAST_ALBUM_ID, albumID)
        editor.apply()
    }

    private fun getLastSavedAlbumID() : Long? {
        val sharedPreferences = context.getSharedPreferences(MUSIC_PREFERENCES, Context.MODE_PRIVATE)
        val albumID = sharedPreferences.getLong(KEY_LAST_ALBUM_ID, -1)
        if(albumID < 0) {
            return null
        }
        return albumID
    }

    override fun getAlbums(): Single<List<Album>> {
        val albumCursor = context.contentResolver.query(
            externalUri, albumProjections, null, null, null)

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
        getLastSavedAlbumID()?.let { albumID ->
            return getSongs(albumID)
        } ?: return Single.just(listOf())
    }
}
package com.whisker.mrr.music.repository

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.whisker.mrr.domain.model.Album
import com.whisker.mrr.domain.model.Artist
import com.whisker.mrr.domain.model.Song
import com.whisker.mrr.domain.repository.MusicRepository
import io.reactivex.Observable
import io.reactivex.Single

class MusicDataRepository(private val context: Context) : MusicRepository {

    private val songsProjection by lazy {
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

    private val albumsProjection by lazy {
        arrayOf(
            MediaStore.Audio.Albums._ID,
            MediaStore.Audio.Albums.ALBUM,
            MediaStore.Audio.Albums.ARTIST,
            MediaStore.Audio.Albums.NUMBER_OF_SONGS,
            MediaStore.Audio.Albums.ALBUM_ART
        )
    }

    private val artistsProjection by lazy {
        arrayOf(
            MediaStore.Audio.Artists._ID,
            MediaStore.Audio.Artists.ARTIST,
            MediaStore.Audio.Artists.ARTIST_KEY,
            MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
            MediaStore.Audio.Artists.NUMBER_OF_TRACKS
        )
    }


    override fun getSongs(albumID: Long?): Single<List<Song>> {
        return Single.fromCallable {
            val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            var selection: String? = null
            var selectionArgs: Array<String>? = null

            albumID?.let { id ->
                selection = MediaStore.Audio.Media.ALBUM_ID + "= ?"
                selectionArgs = arrayOf(id.toString())
            } ?: run {
                selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0 AND " +
                        MediaStore.Audio.Media.DISPLAY_NAME + " LIKE '%.mp3'"
            }

            fetchSongs(externalUri, selection, selectionArgs)
        }
    }

    override fun getSongsByArtistID(artistID: Long): Single<List<Song>> {
        return Single.fromCallable {
            val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            val selection = MediaStore.Audio.Media.ARTIST_ID + "= ?"
            val selectionArgs = arrayOf(artistID.toString())

            fetchSongs(externalUri, selection, selectionArgs)
        }
    }

    private fun fetchSongs(externalUri: Uri, selection: String?, selectionArgs: Array<String>?) : List<Song> {
        val musicCursor: Cursor? = context.contentResolver.query(
            externalUri, songsProjection, selection, selectionArgs, null)
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
        return songsList
    }

    override fun getAlbums(artistName: String?): Single<List<Album>> {
        return Single.fromCallable {
            val externalUri: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

            var selection: String? = null
            var selectionArgs: Array<String>? = null

            if(artistName != null) {
                selection = MediaStore.Audio.Albums.ARTIST + "= ?"
                selectionArgs = arrayOf(artistName)
            }
            val albumCursor = context.contentResolver.query(
                externalUri, albumsProjection, selection, selectionArgs, null)

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
            albumsList
        }
    }

    override fun getArtists(): Single<List<Artist>> {
        return Single.fromCallable {
            val artistList: MutableList<Artist> = mutableListOf()
            val externalUri: Uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI
            val artistCursor = context.contentResolver.query(
                externalUri, artistsProjection, null, null, null
            )

            artistCursor?.let { cursor ->
                while(cursor.moveToNext()) {
                    artistList.add(
                        Artist(
                            cursor.getLong(0),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getInt(3),
                            cursor.getInt(4)
                        )
                    )
                }
            }

            artistCursor?.close()
            artistList
        }
        .flatMapObservable { Observable.fromIterable(it) }
        .flatMapSingle { artist ->
            getAlbums(artist.artistName).map {
                artist.albums = it
                artist
            }
        }.toList()
    }

    override fun getLastPlaylist(): Single<List<Song>> {
        return Single.just(listOf())
    }
}
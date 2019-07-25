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


    override fun getSongs(albumID: Long?): Single<List<Song>> {
        return Single.fromCallable {
            val externalUri: Uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI

            val songsProjection = arrayOf(
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
            songsList
        }

    }

    override fun getAlbums(artistID: String?): Single<List<Album>> {
        return Single.fromCallable {
            val externalUri: Uri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI

            val albumsProjection = arrayOf(
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ALBUM_ART
            )

            var selection: String? = null
            var selectionArgs: Array<String>? = null

            if(artistID != null) {
                selection = MediaStore.Audio.Albums.ARTIST + "= ?"
                selectionArgs = arrayOf(artistID)
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
            val externalUri: Uri = MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI

            val artistProjection = arrayOf(
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
            )
            val artistCursor = context.contentResolver.query(
                externalUri, artistProjection, null, null, null
            )

            val artistList: MutableList<Artist> = mutableListOf()

            artistCursor?.let { cursor ->
                artistList.add(
                    Artist(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3)
                    )
                )
            }

            artistCursor?.close()
            artistList
        }
        .flatMapObservable { Observable.fromIterable(it) }
        .flatMapSingle {  artist ->
            getAlbums(artist.artistKey).map {
                artist.albums = it
                artist
            }
        }.toList()
    }

    override fun getLastPlaylist(): Single<List<Song>> {
        return Single.just(listOf())
    }
}
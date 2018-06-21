package com.lingsatuo.songscan

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore


class SongScan private constructor(private var context: Context) {
    companion object {
        private var Album = HashMap<String, ArrayList<Song>>()
        fun scan(context: Context): SongScan {
            val scan = SongScan(context)
            scan.startScan()
            return scan
        }
    }

    @SuppressLint("Recycle")
    private fun startScan() {
        Album.clear()
        val cursor = context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.MIME_TYPE,
                MediaStore.Audio.Media.SIZE,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ALBUM_ID
        ), null, null, null)
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3)<1000*60)continue
                val song = Song(cursor.getString(2) ?: "Unknown",
                        cursor.getString(1) ?: "Unknown", cursor.getInt(3), cursor.getString(4) ?: "Unknown",
                        cursor.getString(5) ?: "Unknown", cursor.getString(6)
                        ?: "Unknown", "Music", (cursor.getInt(8) / 1024f / 1024f).toString().substring(0, 4) + "M",
                        cursor.getString(9) ?: "Unknown",getAlbumIcon(cursor.getInt(10)))
                if (Album.containsKey(song.getAlbum())) {
                    Album[song.getAlbum()]?.add(song)
                } else {
                    val list = ArrayList<Song>()
                    list.add(song)
                    Album[song.getAlbum()] = list
                }
            } while (cursor.moveToNext())
            cursor.close()
        }
    }
    private fun getAlbumIcon(id:Int):String{
        val mUriAlbums = "content://media/external/audio/albums"
        val projection = arrayOf("album_art")
        val cur = context.contentResolver.query(Uri.parse("$mUriAlbums/$id"), projection, null, null, null)
        var album_art: String? = null
        if (cur.count > 0 && cur.columnCount > 0) {
            cur.moveToNext()
            album_art = cur.getString(0)
        }
        if (album_art!=null) album_art = "file://$album_art"
        cur.close()
        return album_art?:"http://musicone-1253269015.coscd.myqcloud.com/9AB05C66154C4C2D975C53B3BFC43E76.png"
    }
    fun getAlbum(): HashMap<String, ArrayList<Song>> = Album
}
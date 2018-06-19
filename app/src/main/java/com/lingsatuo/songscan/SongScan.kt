package com.lingsatuo.songscan

import android.annotation.SuppressLint
import android.content.Context
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
                MediaStore.Audio.Media.DATA
        ), null, null, null)
        if (cursor.moveToFirst()) {
            do {
                if(cursor.getInt(3)<1000*60)continue
                val song = Song(cursor.getString(2) ?: "Unknown",
                        cursor.getString(1) ?: "Unknown", cursor.getInt(3), cursor.getString(4) ?: "Unknown",
                        cursor.getString(5) ?: "Unknown", cursor.getString(6)
                        ?: "Unknown", "Music", (cursor.getInt(8) / 1024f / 1024f).toString().substring(0, 4) + "M",
                        cursor.getString(9) ?: "Unknown")
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

    fun getAlbum(): HashMap<String, ArrayList<Song>> = Album
}
package com.lingsatuo.utils

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.Toast
import com.lingsatuo.getqqmusic.GetMusicAbsPath
import com.lingsatuo.getqqmusic.GetMusicFileName
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.thieves.R
import com.tbruyelle.rxpermissions2.RxPermissions
import java.io.File

class MusicDownPop(private var activity: Activity, private var item: MusicItem) : View.OnClickListener {
    private var qua = GetMusicFileName.Quality.MP3
    override fun onClick(v: View?) {
        if (v == null) return
        qua = when (v.id) {
            R.id.dl_m4a_l -> GetMusicFileName.Quality.M4AL
            R.id.dl_m4a -> GetMusicFileName.Quality.M4AH
            R.id.dl_ogg -> GetMusicFileName.Quality.OGG
            R.id.dl_mp3 -> GetMusicFileName.Quality.MP3
            R.id.dl_mp3h -> GetMusicFileName.Quality.MP3H
            R.id.dl_fac -> GetMusicFileName.Quality.FLAC
            R.id.dl_ape -> GetMusicFileName.Quality.APE
            else -> GetMusicFileName.Quality.MP3
        }
        GetMusicAbsPath(item, qua, { path ->
            RunOnUiThread {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val rxPermissions = RxPermissions(activity)
                    rxPermissions
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE
                                    , Manifest.permission.READ_PHONE_STATE)
                            .subscribe({ get ->
                                if (get) {
                                    download(path)
                                }
                            })
                } else {
                    download(path)
                }
            }
        }).start()
    }

    private var pop: PopupWindow
    private val view: View = LayoutInflater.from(activity).inflate(R.layout.download_layout, null, false)

    init {
        pop = PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        pop.isOutsideTouchable = true
        pop.setBackgroundDrawable(ColorDrawable(0x00ffffff))
        pop.animationStyle = R.style.down_load_menu
    }

    fun show(v: View) {
        if (item.isloca) {
            Toast.makeText(activity,"你去桶里打桶水吧！（这是本地歌曲）",Toast.LENGTH_SHORT).show()
            return
        }
        pop.showAtLocation(v, Gravity.BOTTOM, 0, 0)
        view.findViewById<LinearLayout>(R.id.dl_m4a_l).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.dl_m4a).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.dl_mp3).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.dl_ogg).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.dl_mp3h).setOnClickListener(this)
        view.findViewById<LinearLayout>(R.id.dl_fac).setOnClickListener {}
        view.findViewById<LinearLayout>(R.id.dl_ape).setOnClickListener {}
    }

    private fun download(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle(item.title)
        request.setDescription("MusicOne克隆")
        val file = File(Environment.getExternalStorageDirectory(), "MusicOne/songs")
        if (!file.exists()) {
            file.mkdirs()
        }
        var n = item.filename.split(".")
        val df = File(file, item.title + "_" + item.getSingers() + "." + (n[n.size - 1]))
        request.setDestinationUri(Uri.fromFile(df))
        val manager = activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
        Toast.makeText(activity, "已经提交系统处理", Toast.LENGTH_LONG).show()
    }
}
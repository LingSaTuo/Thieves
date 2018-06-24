package com.lingsatuo.utils

import android.Manifest
import android.app.Activity
import android.app.DownloadManager
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.support.v7.app.AlertDialog
import android.view.*
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
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

    private val view: View = LayoutInflater.from(activity).inflate(R.layout.download_layout, null, false)
    private var root:LinearLayout
    init {
        root = view.findViewById(R.id.down_root)
    }

    fun showDialog(){
        if (item.isloca) {
            Toast.makeText(activity, "你去桶里打桶水吧！（这是本地歌曲）", Toast.LENGTH_SHORT).show()
            return
        }
        val dia = AlertDialog.Builder(view.context)
                .setView(view)
                .show()
        init()
        dia.window.setGravity(Gravity.BOTTOM)
        dia.window.setWindowAnimations(R.style.down_load_menu)
        dia.window.setBackgroundDrawableResource(R.drawable.share_card)
    }



    private fun init() {
        for (key in item.filesize.keys) {
            when(key){
                GetMusicFileName.Quality.MP3->{
                    root.getChildAt(3).visibility = View.VISIBLE
                    root.getChildAt(3).setOnClickListener(this)
                    addsize(root.getChildAt(3) as LinearLayout, item.filesize[GetMusicFileName.Quality.MP3])
                }
                GetMusicFileName.Quality.M4AL->{
                    root.getChildAt(0).visibility = View.VISIBLE
                    root.getChildAt(0).setOnClickListener(this)
                    addsize(root.getChildAt(0) as LinearLayout, item.filesize[GetMusicFileName.Quality.M4AL])
                }
                GetMusicFileName.Quality.M4AH->{
                    root.getChildAt(1).visibility = View.VISIBLE
                    root.getChildAt(1).setOnClickListener(this)
                    addsize(root.getChildAt(1) as LinearLayout, item.filesize[GetMusicFileName.Quality.MP3H])
                }
                GetMusicFileName.Quality.APE->{
                    root.getChildAt(6).visibility = View.VISIBLE
                    root.getChildAt(6).setOnClickListener(this)
                    addsize(root.getChildAt(6) as LinearLayout, item.filesize[GetMusicFileName.Quality.APE])
                }
                GetMusicFileName.Quality.FLAC->{
                    root.getChildAt(5).visibility = View.VISIBLE
                    root.getChildAt(5).setOnClickListener(this)
                    addsize(root.getChildAt(5) as LinearLayout, item.filesize[GetMusicFileName.Quality.FLAC])
                }
                GetMusicFileName.Quality.MP3H->{
                    root.getChildAt(4).visibility = View.VISIBLE
                    root.getChildAt(4).setOnClickListener(this)
                    addsize(root.getChildAt(4) as LinearLayout, item.filesize[GetMusicFileName.Quality.MP3H])
                }
                GetMusicFileName.Quality.OGG->{
                    root.getChildAt(2).visibility = View.VISIBLE
                    root.getChildAt(2).setOnClickListener(this)
                    addsize(root.getChildAt(2) as LinearLayout, item.filesize[GetMusicFileName.Quality.OGG])
                }
            }
        }
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
    private fun addsize(view:LinearLayout,size:String?){
        val textView = view.getChildAt(0) as TextView
        textView.append("  $size")
    }
}
package com.lingsatuo.floatwindow

import android.app.Activity
import android.os.Build
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatSeekBar
import android.view.LayoutInflater
import android.view.View
import android.widget.SeekBar
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.lingsatuo.service.MusicService
import com.lingsatuo.thieves.Controller
import com.lingsatuo.thieves.R
import com.lingsatuo.widget.XTextView
import jp.wasabeef.glide.transformations.BlurTransformation

class FloatWindow(private var activity: Activity) {
    private var seekBar:AppCompatSeekBar? = null
    private var ontouch = false
    private val view: View = LayoutInflater.from(activity).inflate(R.layout.float_window,null,false)
    private val bandprogress: (Int, Int, String, String) -> Unit = { c, d, n, r ->
        seekBar?.max = d
        view.findViewById<XTextView>(R.id.float_window_now).text = n
        view.findViewById<XTextView>(R.id.float_window_max).text = r
        if (!ontouch) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                seekBar?.setProgress(c, true)
            } else {
                seekBar?.progress = c
            }
        }
    }
    fun show(){
           AlertDialog.Builder(activity)
                   .setView(view)
                   .setTitle("View测试")
                   .show()
                   .setOnDismissListener({
                       Controller.removeBandProgress(bandprogress)
                   })
        view.findViewById<XTextView>(R.id.float_window_title).text = MusicService.instance?.item?.title
        view.findViewById<XTextView>(R.id.float_window_subtitle).text = MusicService.instance?.item?.getSingers()


        Glide.with(activity)
                .load(MusicService.instance?.item?.albumicon)
                .bitmapTransform(BlurTransformation(activity, 25), CenterCrop(activity))
                .placeholder(R.mipmap.back2)
                .error(R.mipmap.back2)
                .priority(Priority.HIGH)
                .into(view.findViewById(R.id.float_window_back))

        Glide.with(activity)
                .load(MusicService.instance?.item?.albumicon)
                .asBitmap()
                .placeholder(R.mipmap.back2)
                .error(R.mipmap.back2)
                .priority(Priority.HIGH)
                .into(view.findViewById(R.id.float_window_icon))

        seekBar = view.findViewById(R.id.float_window_seekbar)
        seekBar!!.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                ontouch = true
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                ontouch = false
                MusicService.player.seekTo(seekBar?.progress ?: 0)
            }

        })
        Controller.bandProgress(bandprogress)
    }
}
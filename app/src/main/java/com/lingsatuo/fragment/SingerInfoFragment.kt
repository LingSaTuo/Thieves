package com.lingsatuo.fragment

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import com.lingsatuo.getqqmusic.MusicItem
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.thieves.R
import com.lingsatuo.widget.XTextView
import com.lingsatuo.widget.XWebView
import org.jsoup.Jsoup

class SingerInfoFragment : Fragment() {
    private var activity: Activity? = null
    companion object {
        fun getInstance(title: String, type: Int): SingerInfoFragment {
            val mBundle = MBundle(title, type)
            val fragment = SingerInfoFragment()
            val bundle = Bundle()
            bundle.putSerializable("bundle", mBundle)
            fragment.arguments = bundle
            return fragment
        }
    }

    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.singer_info_fragment,container,false)
        if (activity == null)return v
        val singer = activity!!.intent.getSerializableExtra("item") as MusicItem.Singer
        val web = v.findViewById<XWebView>(R.id.loading_info)
        web.addFinishListener {
            val info = readSingerMessage(web.getSource())
            RunOnUiThread{
                v.findViewById<XTextView>(R.id.singer_info_fragment_title).text = info
                v.findViewById<ProgressBar>(R.id.loading_).visibility = View.GONE
                v.findViewById<XTextView>(R.id.singer_info_fragment_title).visibility = View.VISIBLE
            }
        }
        web.load(singer.singerhref)
        return v
    }
    private fun readSingerMessage(html:String):String{
        val jsoup = Jsoup.parse(html)
        val singerinfo = jsoup.select("div.data__desc_txt").text().replace(">","\n\t").replace("\t","\n\t")
        return singerinfo
    }
}
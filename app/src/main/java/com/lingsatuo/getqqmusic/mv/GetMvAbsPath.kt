package com.lingsatuo.getqqmusic.mv

import com.lingsatuo.getqqmusic.GetJsonObj
import com.lingsatuo.getqqmusic.RunOnUiThread
import com.lingsatuo.utils.NetWork

class GetMvAbsPath(private var mvItem: MvItem, private var listener: (String, Throwable?) -> Unit) : Thread() {
    private val href = "https://vv.video.qq.com/getinfo?" +
            "vid=${mvItem.mvid}&" +
            "otype=json&guid=21b1d1487c0e3886a2958dc5f2503b9a&" +
            "platform=11&defnpayver=1&appver=3.2.19.333" +
            "sdtfrom=v1010&_qv_rmt=jBZ5pIl8A19213wFt=&_qv_rmt2=o6Qa4YDm149352oVg="

    override fun run() {
        super.run()
        try {
            val string = NetWork().get(href,"pgv_pvi=9283530752;pgv_si=s4698655744")
            val jsonObject = GetJsonObj.get(string)
            val vl = jsonObject.getJSONObject("vl")
            val mv0 = vl.getJSONArray("vi").getJSONObject(0)
            mvItem.fmd5 = mv0.getString("fmd5")
            val fi = jsonObject.getJSONObject("fl").getJSONArray("fi")
            val id = fi.getJSONObject(fi.length() - 1).getInt("id")
            mvItem.filename = mvItem.mvid + ".p${id % 1000}.1.mp4"
            mvItem.filesize = "${String.format("%.2f", (mv0.getInt("fs") / 1024f / 1024))}MB"
            mvItem.fvkey = mv0.getString("fvkey")
            mvItem.lfilename = mv0.getString("fn").replace("mp4","1.mp4")
            mvItem.title = mv0.getString("ti")
            mvItem.startHref = mv0.getJSONObject("ul").getJSONArray("ui").getJSONObject(0).getString("url")
            mvItem.vkey = getKey(mvItem)
            RunOnUiThread {
                listener.invoke(mvItem.toPath(), null)
            }
        } catch (e: Throwable) {
            RunOnUiThread {
                listener.invoke("", e)
            }
        }
    }

    private fun getKey(item: MvItem): String {
        val href = "https://vv.video.qq.com/getkey" +
                "?otype=json&" +
                "platform=11" +
                "&format=10309&" +
                "vid=${item.mvid}&filename=${item.filename}&appver=3.2.19.333"
        val s = NetWork().get(href,"pgv_pvi=9283530752;pgv_si=s4698655744")
        val json = GetJsonObj.get(s)
        return json.getString("key")
    }
}
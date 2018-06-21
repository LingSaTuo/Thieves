package com.lingsatuo.getqqmusic

class VKeyGeter(private var item: MusicItem) {
    private fun getHref() = "https://c.y.qq.com/base/fcgi-bin/fcg_music_express_mobile3.fcg?"
    fun getG_TK() = "5381"
    companion object {
        fun getKeyUrl() = "https://c.y.qq.com/base/fcgi-bin/fcg_musicexpress2.fcg?guid=1513209721127"
    }
    fun getjsonpCallback() = "MusicJsonCallback9052965335600505"
    fun getloginUin() = "0"
    fun gethostUin() = "0"
    fun getformat() = "json"
    fun getinCharset() = "utf8"
    fun getoutCharset() = "utf-8"
    fun getnotice() = "0"
    fun getplatform() = "yqq"
    fun getneedNewCode() = "0"
    fun getCid() = "205361747"
    fun getcallback() = "MusicJsonCallback9052965335600505"
    fun getuin() = "0"
    fun getsingmid() = item.singmid
    fun getfilename() = item.filename
    fun getfromtag() = "53"
    fun getGuid() = "1513209721127"
    override fun toString(): String = "${getHref()}g_tk=${getG_TK()}&jsonpCallback=${getjsonpCallback()}&loginUin=${getloginUin()}&hostUin=${gethostUin()}&format=${getformat()}&inCharset=${getinCharset()}&outCharset=${getoutCharset()}&" +
            "notice=${getnotice()}&platform=${getplatform()}&needNewCode=${getneedNewCode()}&cid=${getCid()}&callback=${getcallback()}&uin=${getuin()}&songmid=${getsingmid()}&filename=${getfilename()}&guid=${getGuid()}"
}
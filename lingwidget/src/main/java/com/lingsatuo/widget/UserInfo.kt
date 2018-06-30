package com.lingsatuo.widget

class UserInfo(private var href: String) {
    val map = HashMap<String,String>()
    fun toHref() = href
    init {
        val newhref = href.substring("https://c.y.qq.com/rsc/fcgi-bin/fcg_get_profile_homepage.fcg?".length, href.length)
        val list = newhref.split("&")
        for (key in list) {
            map[key.split("=")[0]] = key.split("=")[1]
        }
    }
}
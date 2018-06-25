package com.lingsatuo.getqqmusic

import android.os.Environment
import com.lingsatuo.adapter.UserRvAdapter
import com.lingsatuo.utils.NetWork
import java.io.File

class FindUserList(private var user:UserRvAdapter.User?,private var listener:(ArrayList<UserRvAdapter.User>)->Unit):Thread() {
    override fun run() {
        super.run()
        try{
            val list = find()
            RunOnUiThread{
                listener.invoke(list)
            }
        }catch (e:Throwable){
            RunOnUiThread{
                listener.invoke(ArrayList())
            }
        }
    }
    private fun find():ArrayList<UserRvAdapter.User>{
        val list = ArrayList<UserRvAdapter.User>()
        val files = File(Environment.getExternalStorageDirectory(),"/tencent/MobileQQ/").listFiles()

        if (user!=null&&File(Environment.getExternalStorageDirectory(),"/tencent/MobileQQ/${user!!.qqnum}").exists()){
            list.add(getInfo(user!!.qqnum))
            return list
        }
        for (file in files){
            if (file.name.matches("[0-9]{6,}".toRegex())){
                list.add(getInfo(file.name))
            }
        }
        return list
    }

    private fun getInfo(num:String):UserRvAdapter.User{
        val user = UserRvAdapter.User()
        user.qqnum = num
        getUserInfo(user)
        return user
    }
    private fun getUserInfo(user:UserRvAdapter.User){
        val href = "https://c.y.qq.com/rsc/fcgi-bin/fcg_get_profile_homepage.fcg?" +
                "g_tk=943359060&jsonpCallback=MusicJsonCallback24309135807588123&" +
                "loginUin=${user.qqnum}&hostUin=0&format=jsonp&" +
                "inCharset=utf8&outCharset=utf-8&notice=0&platform=yqq&needNewCode=0&" +
                "cid=205360838&ct=20&userid=${user.qqnum}&reqfrom=1&reqtype=0"
        val str = String(NetWork().getBytes(href))
        val json = GetJsonObj.get(str)
        val data = json.getJSONObject("data")
        val creator = data.getJSONObject("creator")
        user.name = creator.getString("nick")
        user.usericon = creator.getString("headpic")
        //喜欢
        try {
            val list = data.getJSONArray("mymusic").getJSONObject(0)
            user.cfinfowebhref = creator.getString("cfinfo")
            val group = MusicGroup()
            group.disstid = list.getInt("id").toString()
            group.icon = list.getString("picurl")
            group.amount = list.getString("subtitle")
            group.num = list.getInt("num0")
            group.title = "我的喜欢"
            user.list.add(group)
        }catch (e:Throwable){}
        //列表
        val mydiss = data.getJSONObject("mydiss").getJSONArray("list")
        for (index in 0 until mydiss.length()){
            val musicgroup = MusicGroup()
            val jobj = mydiss.getJSONObject(index)
            musicgroup.disstid = jobj.getString("dissid")
            musicgroup.icon = jobj.getString("picurl")
            musicgroup.title = jobj.getString("title")
            musicgroup.amount = jobj.getString("subtitle")
            user.list.add(musicgroup)
        }
    }
}
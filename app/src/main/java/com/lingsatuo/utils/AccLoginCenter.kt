package com.lingsatuo.utils

import com.lingsatuo.adapter.UserRvAdapter

object AccLoginCenter {
    private val listeners = ArrayList<(UserRvAdapter.User)->Unit>()
    var last = UserRvAdapter.User()
    fun invoke(user:UserRvAdapter.User){
        last = user
        for (index in listeners)
            index.invoke(user)
    }
    fun addOnLoginListener(listener:(UserRvAdapter.User)->Unit){
        if(!listeners.contains(listener)){
            listeners.add(listener)
        }
    }
    fun removeListener(listener:(UserRvAdapter.User)->Unit){
        if(listeners.contains(listener)){
            listeners.remove(listener)
        }
    }
}
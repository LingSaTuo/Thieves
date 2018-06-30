package com.lingsatuo.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.Priority
import com.lingsatuo.getqqmusic.MusicGroup
import com.lingsatuo.thieves.R
import com.lingsatuo.utils.AccLoginCenter
import com.lingsatuo.widget.UserInfo
import com.lingsatuo.widget.XTextView
import de.hdodenhof.circleimageview.CircleImageView
import java.util.ArrayList

class UserRvAdapter(var context: Context) : RecyclerView.Adapter<UserIcon>() {
    private val userlist = ArrayList<User>()
    private var listener : (Int)->Unit={_-> }
    fun setUserList(users:ArrayList<User>){
        this.userlist.clear()
        this.userlist.addAll(users)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserIcon {
        return UserIcon(LayoutInflater.from(context).inflate(R.layout.item_user,parent,false),listener)
    }
    fun setOnItemClickListener(lis: (Int)->Unit){
        this.listener = lis
    }
    override fun getItemCount(): Int = this.userlist.size
    fun getItem(postion:Int) : User{
        return this.userlist[postion]
    }
    override fun onBindViewHolder(holder: UserIcon, position: Int) {
        Glide.with(context)
                .load(this.userlist[position].usericon)
                .asBitmap()
                .placeholder(R.mipmap.user_icon)
                .priority(Priority.HIGH)
                .error(R.mipmap.user_icon)
                .into(holder.getUserIcon())
        holder.getUserName().text = this.userlist[position].name
        if (AccLoginCenter.last.qqnum == this.userlist[position].qqnum){
            holder.getUserName().setTextColor(context.resources.getColor(R.color.colorAccent))
        }else{
            holder.getUserName().setTextColor(context.resources.getColor(R.color.button_textColor))
        }
    }
    class User{
        var name = ""
        var qqnum=""
        var cid = ""
        var usericon=""
        var cfinfowebhref = ""
        val list = ArrayList<MusicGroup>()
    }
}
class UserIcon(private var view: View, listener: (Int) -> Unit) : RecyclerView.ViewHolder(view){
    fun getUserIcon()=view.findViewById<CircleImageView>(R.id.user_icon)
    fun getUserName()= view.findViewById<XTextView>(R.id.user_name)
    init {
        view.setOnClickListener { v->
            listener.invoke(position)
        }
    }
}
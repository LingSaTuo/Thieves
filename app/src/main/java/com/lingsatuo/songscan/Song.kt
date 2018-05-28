package com.lingsatuo.songscan

class Song {
    private var title = ""
    private var filename = ""
    private var duration = 0
    private var singer = ""
    private var album = ""
    private var year = ""
    private var type = ""
    private var size = ""
    private var fileurl = ""
    constructor():this("","",0,"","","","","","")
    constructor(title:String,filename:String,duration:Int,singer:String,album:String,year:String,type:String,size:String,fileurl :String){
        this.title = title
        this.filename = filename
        this.duration = duration
        this.singer = singer
        this.year = year
        this.type = type
        this.album = album
        this.size = size
        this.fileurl = fileurl
    }
    fun setAlbum(album: String){
        this.album
    }
    fun setSize(size: String){
        this.size = size
    }
    fun setFileUrl(fileurl: String){
        this.fileurl = fileurl
    }
    fun setTitle(title: String){
        this.title = title
    }
    fun setFileName(filename:String){
        this.filename = filename
    }
    fun setDuraation(duration: Int){
        this.duration = duration
    }
    fun setSinger(singer: String){
        this.singer = singer
    }
    fun setYear(year: String){
        this.year = year
    }
    fun setType(type: String){
        this.type = type
    }
    fun getTitle() = this.title
    fun getFileName() = this.filename
    fun getduration() = this.duration
    fun getSinger() = this.singer
    fun getAlbum() = this.album
    fun getType() = this.type
    fun getYear() = this.year
    fun getSize() = this.size
    fun getFileUrl() = this.fileurl
    override fun toString(): String {
        return "$title   $singer   $album"
    }
}
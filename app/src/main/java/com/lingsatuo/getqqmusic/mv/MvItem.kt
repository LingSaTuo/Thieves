package com.lingsatuo.getqqmusic.mv

class MvItem {
    var title = ""//MV标题
    var fvkey = ""//mv的fVkey
    var vkey = ""//mv的Vkey
    var lfilename = ""//第二画质
    var fmd5 = ""//md5校验
    var filename = ""//文件名字
    var mvid = ""//mv的id
    var filesize = ""//文件大小
    var startHref=""//地址前缀
    var other = "&platform=70202&br=85&fmt=fhd&sp=0&charge=0&vip=0&guid=AF55369CA1166F5BCB0AB37D8DC45124&ocid=2545030572&ocid=783488428"

    fun toPath() = "$startHref$filename?sdtfrom=v1070&type=mp4&vkey=$vkey$other"
}
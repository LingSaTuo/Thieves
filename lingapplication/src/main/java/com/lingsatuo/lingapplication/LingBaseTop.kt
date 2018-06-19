package com.lingsatuo.lingapplication

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Process
import android.support.v7.app.AppCompatActivity

/* *                #                                                   #
* #                       _oo0oo_                     #
* #                      o8888888o                    #
* #                      88" . "88                    #
* #                      (| -_- |)                    #
* #                      0\  =  /0                    #
* #                    ___/`---'\___                  #
* #                  .' \\|     |# '.                 #
* #                 / \\|||  :  |||# \                #
* #                / _||||| -:- |||||- \              #
* #               |   | \\\  -  #/ |   |              #
* #               | \_|  ''\---/''  |_/ |             #
* #               \  .-\__  '-'  ___/-. /             #
* #             ___'. .'  /--.--\  `. .'___           #
* #          ."" '<  `.___\_<|>_/___.' >' "".         #
* #         | | :  `- \`.;`\ _ /`;.`/ - ` : | |       #
* #         \  \ `_.   \_ __\ /__ _/   .-` /  /       #
* #     =====`-.____`.___ \_____/___.-`___.-'=====    #
* #                       `=---='                     #
* #     ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~   #
* #                                                   #
* #               佛祖保佑         永无BUG              #
* #                                                   #
*/
/**
 * Created by Administrator on 2018/3/25.
 */
open class LingBaseTop : Application() {
    override fun onCreate() {
        super.onCreate()
        UncaughtExceptionHandler.getInstance(this).init()
    }
    companion object {
        private val list = ArrayList<AppCompatActivity>()
        fun addActivity(activity: AppCompatActivity){
            if (list.contains(activity))return
            list.add(activity)
        }
        fun exitApp(){
            for (activity in list){
                activity.onBackPressed()
                activity.finish()
            }
        }
    }
}
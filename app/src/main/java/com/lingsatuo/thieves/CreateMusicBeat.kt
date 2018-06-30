package com.lingsatuo.thieves

import android.os.Bundle
import com.lingsatuo.app.BaseActivity

class CreateMusicBeat : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        * int height = 0;
int resourceId = getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
if (resourceId > 0) {
    height = getApplicationContext().getResources().getDimensionPixelSize(resourceId);
}
        * */
        val id = applicationContext.resources.getIdentifier("status_bar_height","dimen","android")
        val height = if (id < 0){
            20
        }else{
            applicationContext.resources.getDimensionPixelSize(id)
        }
    }
}
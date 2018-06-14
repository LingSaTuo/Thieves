package com.lingsatuo.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.PagerAdapter
import android.view.ViewGroup
import com.lingsatuo.fragment.MBundle

class SingerViewPagerAdapter(fm:FragmentManager?) : FragmentStatePagerAdapter(fm) {
    private val list = ArrayList<Fragment>()
    override fun getItem(position: Int): Fragment {
        return list[position]
    }
    fun addFragment(fragment:Fragment){
        this.list.add(fragment)
    }

    override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE
    }
    override fun finishUpdate(container: ViewGroup) {
        try {
            super.finishUpdate(container)
        }catch (e:Exception){}
    }

    override fun getPageTitle(position: Int): CharSequence? {
        val bundle = list[position].arguments?.getSerializable("bundle") as MBundle
        return bundle.getTitle()
    }
    override fun getCount(): Int = list.size

}
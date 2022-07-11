package com.leony.app.ricknmorty.adapter

import android.content.Context
import android.content.Context.LAYOUT_INFLATER_SERVICE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

class IntroductionSliderAdapter : PagerAdapter() {
    private var layout: IntArray? = null
    private var context: Context? = null

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val layoutInflater = context!!.getSystemService(
            LAYOUT_INFLATER_SERVICE
        ) as LayoutInflater

        val inflate = layoutInflater.inflate(layout?.get(position) ?: 0,container, false)
        container.addView(inflate)

        return inflate
    }

    override fun getCount(): Int {
        return layout?.size!!
    }

    override fun isViewFromObject(view: View, mObject: Any): Boolean {
        return view == mObject
    }

    override fun destroyItem(container: ViewGroup, position: Int, mObject: Any) {
        val view = mObject as View
        container.removeView(view)
    }

    fun update(layout: IntArray, context: Context){
        this.layout = layout
        this.context = context
    }
}
package com.leony.app.ricknmorty.view

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.adapter.IntroductionSliderAdapter
import com.leony.app.ricknmorty.data.helper.IntroductionManager

class IntroductionActivity : AppCompatActivity() {
    private var viewPager: ViewPager? = null
    private var pointsLayout: LinearLayout? = null
    var btnNextSlide: CardView? = null
    private lateinit var layouts: IntArray
    lateinit var indicators: Array<TextView?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_introduction)
        isFirstTimeLaunched(this)
        initResources()
        selectedIndicator(0)
        setViewPagerListener()
    }

    private fun isFirstTimeLaunched(context: Context){
        val introductionManager = IntroductionManager(context)
        if (introductionManager.checkIsFirstTime()){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            introductionManager.setFirstTimeView(true)
        }
    }

    private fun initResources(){
        viewPager = findViewById(R.id.introImageViewPager)
        pointsLayout = findViewById(R.id.introImagePoints)
        btnNextSlide = findViewById(R.id.btnFinalIntroduction)
        layouts = intArrayOf(R.layout.introduction_1, R.layout.introduction_2, R.layout.introduction_3)
    }

    private fun setViewPagerListener(){
        val introductionSliderAdapter = IntroductionSliderAdapter()
        introductionSliderAdapter.update(layouts, this)
        viewPager!!.adapter = introductionSliderAdapter
        viewPager!!.addOnPageChangeListener(viewListener)
        btnNextSlide!!.setOnClickListener {
            val currentItem = getItemIndicators(+1)
            if (currentItem < layouts.size){
                viewPager!!.currentItem = currentItem
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun selectedIndicatorView(){
        pointsLayout?.removeAllViews()
        for (i in indicators.indices) {
            indicators[i] = TextView(this)
            indicators[i]?.text = Html.fromHtml("&#9679;")
            indicators[i]?.textSize = MainActivity.TEXT_SIZE.toFloat()
            pointsLayout?.addView(indicators[i])
        }
    }

    private fun selectedIndicator(position: Int) {
        indicators = arrayOfNulls(layouts.size)
        selectedIndicatorView()
        for (i in indicators.indices) {
            if (i == position) {
                indicators[i]?.setTextColor(ContextCompat.getColor(this,R.color.green_slime))
            } else {
                indicators[i]?.setTextColor(ContextCompat.getColor(this,R.color.white))
            }
        }
    }

    private fun getItemIndicators(item: Int): Int {
        return viewPager!!.currentItem + item
    }

    var viewListener: ViewPager.OnPageChangeListener = object : ViewPager.OnPageChangeListener {
        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {

        }

        override fun onPageSelected(position: Int) {
            selectedIndicator(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
    }
}
package com.leony.app.ricknmorty.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.adapter.GetCharacterListAdapter
import com.leony.app.ricknmorty.adapter.ImageSliderAdapter
import com.leony.app.ricknmorty.data.helper.InternetConnectionHelper
import com.leony.app.ricknmorty.data.listener.GetCharacterListItemClickListener
import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import com.leony.app.ricknmorty.data.repository.GetCharacterListRepositoryImpl
import com.leony.app.ricknmorty.data.repository.GetCharacterListView
import com.leony.app.ricknmorty.worker.GetCharacterListWorker
import io.reactivex.schedulers.Schedulers
import java.util.*

class MainActivity : AppCompatActivity(), GetCharacterListView {
    companion object {
        const val ID = "id"
        const val TEXT_SIZE = 12
    }

    private lateinit var mainImageViewPager: ViewPager2
    private lateinit var mainImagePoints: LinearLayout
    private lateinit var mainDataRecyclerView: RecyclerView
    private lateinit var getCharacterListAdapter: GetCharacterListAdapter
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var internetConnectionHelper: InternetConnectionHelper
    lateinit var indicators: Array<TextView?>

    private val getCharacterListWorker by lazy {
        GetCharacterListWorker(
            resources,
            GetCharacterListRepositoryImpl(this, Schedulers.io(), Schedulers.computation()),
            this,
            internetConnectionHelper,
            Schedulers.computation()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        internetConnectionHelper = InternetConnectionHelper(this)

        initResource()
        initRecyclerView()
        initImageSlider()
    }

    private fun initResource(){
        mainImageViewPager = findViewById(R.id.mainImageViewPager)
        mainImagePoints = findViewById(R.id.mainImagePoints)
        mainDataRecyclerView = findViewById(R.id.mainDataRecyclerView)
        imageSliderAdapter = ImageSliderAdapter()
    }

    private fun initRecyclerView(){
        val linearLayoutManager = LinearLayoutManager(this.applicationContext)
        mainDataRecyclerView.layoutManager = linearLayoutManager
        mainDataRecyclerView.hasFixedSize()
        mainDataRecyclerView.apply {
            getCharacterListAdapter = GetCharacterListAdapter(object : GetCharacterListItemClickListener {
                override fun onClickedCharacterId(id: Int) {
                    val intent = Intent(context, DetailActivity::class.java).apply  {
                        addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                    }
                    intent.putExtra(ID, id)
                    startActivity(intent)
                }

            }, context)
        }
        mainDataRecyclerView.adapter = getCharacterListAdapter
    }

    private fun initImageSlider(){
        indicators = arrayOfNulls(resources.getStringArray(R.array.mainImageItems).toList().size)
        imageSliderAdapter.imageSliderAdapter(resources.getStringArray(R.array.mainImageItems).toList())
        mainImageViewPager.adapter = imageSliderAdapter
        selectedIndicatorView()
        mainImageViewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                selectedIndicator(position)
            }
        })
    }

    private fun selectedIndicator(position: Int) {
        for (i in indicators.indices) {
            if (i == position) {
                indicators[i]?.setTextColor(ContextCompat.getColor(this,R.color.green_slime))
            } else {
                indicators[i]?.setTextColor(ContextCompat.getColor(this,R.color.white))
            }
        }
    }

    private fun selectedIndicatorView(){
        mainImagePoints.removeAllViews()
        for (i in indicators.indices) {
            indicators[i] = TextView(this)
            indicators[i]?.text = Html.fromHtml("&#9679;")
            indicators[i]?.textSize = TEXT_SIZE.toFloat()
            mainImagePoints.addView(indicators[i])
        }
    }

    override fun displayCharacterListResult(response: GetCharacterListResponse) {
        runOnUiThread {
            getCharacterListAdapter.updateListCharacter(response)
        }
    }

    override fun displayCharacterListError(error: String) {
        runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        getCharacterListWorker.execute()
    }

    override fun onStop() {
        super.onStop()
        getCharacterListWorker.stopWorker()
    }
}
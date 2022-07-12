package com.leony.app.ricknmorty.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
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
    /**
     * ID : is a key to pass from MainActivity to DetailActivity when the user
     * clicked on the character item.
     */
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

    /**
     * Here, I initialized a worker variable that responsible for the business logic.
     * I called this variable in onStart() override method. Usually, the proper way to do that
     * is to register the business logic itself first in the onStart() then we could call the action needed
     * in every single request we ask in the Activity Class.
     *
     * To do this, we must provide a project that contains only an action type, a standardized data class, a worker class, and an action class.
     * E.g:
     * TypeAction class: TypeAction.ON_REQUEST_CHARACTER_LIST, TypeAction.REQUEST_CHARACTER_LIST
     * TypeAction.ON_SUCCESS_RETRIEVE_CHARACTER_LIST, TypeAction.ON_ERROR_RETRIEVE_CHARACTER_LIST
     */
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

        /**
         * This variable is responsible for checking internet connection, details would be explained on the expected class.
         */
        internetConnectionHelper = InternetConnectionHelper(this)

        /**
         * I usually like to separate works into several methods to make the fill of the onCreate() method concise,
         * and also when there are any bugs, I could trace it more clearly.
         */
        initResource()
        initRecyclerView()
        initImageSlider()
    }

    /**
     * This method contains initialization of the attributes.
     */
    private fun initResource(){
        mainImageViewPager = findViewById(R.id.mainImageViewPager)
        mainImagePoints = findViewById(R.id.mainImagePoints)
        mainDataRecyclerView = findViewById(R.id.mainDataRecyclerView)
        imageSliderAdapter = ImageSliderAdapter()
    }

    /**
     * This method contains initialization of the Character List RecyclerView
     */
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

    /**
     * This method is responsible for handling ImageView Slider that I put on the MainActivity.
     * (Three Images located on the top of the Character list)
     */
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

    /**
     * This function is responsible to determine the selected position indicators or well-known as dots
     * on the ImageView Slider
     */
    private fun selectedIndicator(position: Int) {
        for (i in indicators.indices) {
            if (i == position) {
                indicators[i]?.setTextColor(ContextCompat.getColor(this,R.color.green_slime))
            } else {
                indicators[i]?.setTextColor(ContextCompat.getColor(this,R.color.white))
            }
        }
    }

    /**
     * This function is responsible to set the UI of the indicators to set it on the view
     */
    private fun selectedIndicatorView(){
        mainImagePoints.removeAllViews()
        for (i in indicators.indices) {
            indicators[i] = TextView(this)
            indicators[i]?.text = Html.fromHtml("&#9679;")
            indicators[i]?.textSize = TEXT_SIZE.toFloat()
            mainImagePoints.addView(indicators[i])
        }
    }

    /**
     * These are an override method from GetCharacterListView class that I created because of the use of the MVVM Architecture
     */
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

    /**
     * I called the worker in here directly
     */
    override fun onStart() {
        super.onStart()
        getCharacterListWorker.execute()
    }

    /**
     * To prevent memory leaks, I always stop the worker whenever the activity is being closed
     * or the request(s) has been fulfilled.
     */
    override fun onStop() {
        super.onStop()
        getCharacterListWorker.stopWorker()
    }
}
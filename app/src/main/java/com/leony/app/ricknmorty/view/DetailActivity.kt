package com.leony.app.ricknmorty.view

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.data.helper.InternetConnectionHelper
import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse
import com.leony.app.ricknmorty.data.repository.GetCharacterDetailRepositoryImpl
import com.leony.app.ricknmorty.data.repository.GetCharacterDetailView
import com.leony.app.ricknmorty.worker.GetCharacterDetailWorker
import com.squareup.picasso.Picasso
import io.reactivex.schedulers.Schedulers


class DetailActivity : AppCompatActivity(), GetCharacterDetailView {

    private var charId: Int = 0
    private lateinit var charImage: ImageView
    private lateinit var backButton: ImageView
    private lateinit var charName: TextView
    private lateinit var charGender: TextView
    private lateinit var charStatus: TextView
    private lateinit var charSpecies: TextView
    private lateinit var charType: TextView
    private lateinit var charOrigin: TextView
    private lateinit var charLocation: TextView
    private lateinit var charNumOfEpisode: TextView
    private lateinit var episodeRecyclerView: RecyclerView
    private lateinit var internetConnectionHelper: InternetConnectionHelper

    /**
     * A worker variable to handle request for Character Details
     */
    private val getCharacterDetailWorker by lazy {
        GetCharacterDetailWorker(
            resources,
            GetCharacterDetailRepositoryImpl(this, Schedulers.io(), Schedulers.computation()),
            this,
            internetConnectionHelper,
            Schedulers.computation()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        /**
         * A helper class to help checking the internet connection
         */
        internetConnectionHelper = InternetConnectionHelper(this)

        /**
         * initResource(): Initialize attributes IDs
         * initIntent(): Getting the intent passed by the MainActivity
         */
        initResource()
        initIntent()
    }

    private fun initResource(){
        charImage = findViewById(R.id.charImage)
        backButton = findViewById(R.id.backButton)
        charName = findViewById(R.id.charName)
        charGender = findViewById(R.id.charGender)
        charStatus = findViewById(R.id.charStatus)
        charSpecies = findViewById(R.id.detailSpeciesValue)
        charType = findViewById(R.id.detailTypeValue)
        charOrigin = findViewById(R.id.detailOriginValue)
        charLocation = findViewById(R.id.detailLocationValue)
        charNumOfEpisode = findViewById(R.id.detailEpisodeValue)
        episodeRecyclerView = findViewById(R.id.detailEpisodesRecyclerView)

        /**
         * Whenever the user clicked on the back button, it will take them to the MainActivity back.
         */
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun initIntent(){
        /**
         * Getting the ID that being passed by the MainActivity.
         * This ID will be called by the action to request character details from the server
         */
        charId = intent.getIntExtra(MainActivity.ID,0)
    }

    /**
     * These are an override method from GetCharacterDetailView class that I created because of the use of the MVVM Architecture
     */
    override fun displayCharacterDetailResult(response: GetCharacterDetailResponse) {
        runOnUiThread{
            charName.text = response.name
            charGender.text = response.gender
            charStatus.text = response.status
            charSpecies.text = response.species
            charType.text = response.type
            charOrigin.text = response.origin?.name ?: "-"
            charLocation.text = response.location?.name ?: "-"
            charNumOfEpisode.text = response.episodes?.size?.toString()?.let {
                resources.getString(R.string.detailItemNumberEpisodePlaceholder).replace("$1",
                    it
                )
            }
            Picasso.get().load(response.image).placeholder(R.color.dark_gray).error(R.color.dark_gray).into(charImage)
        }
    }

    override fun displayCharacterDetailError(error: String) {
        runOnUiThread {
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * I called the worker in here directly
     */
    override fun onStart() {
        super.onStart()
        getCharacterDetailWorker.execute(charId)
    }

    /**
     * To prevent memory leaks, I always stop the worker whenever the activity is being closed
     * or the request(s) has been fulfilled
     */
    override fun onStop() {
        super.onStop()
        getCharacterDetailWorker.stopWorker()
    }
}
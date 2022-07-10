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

        internetConnectionHelper = InternetConnectionHelper(this)

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

        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    private fun initIntent(){
        charId = intent.getIntExtra(MainActivity.ID,0)
    }

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

    override fun onStart() {
        super.onStart()
        getCharacterDetailWorker.execute(charId)
    }

    override fun onStop() {
        super.onStop()
        getCharacterDetailWorker.stopWorker()
    }
}
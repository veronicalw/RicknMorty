package com.leony.app.ricknmorty.data.repository

import android.content.Context
import com.leony.app.ricknmorty.data.helper.ServerInfo
import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse
import com.leony.app.ricknmorty.data.retrofit.RemoteGeneralDatastore
import io.reactivex.Observable
import io.reactivex.Scheduler

/**
 * Similar to ViewModel class based on MVVM Architecture
 * that implements repository class called GetCharacterListRepository
 */
class GetCharacterDetailRepositoryImpl (val context: Context, val ioScheduler: Scheduler, val  compScheduler: Scheduler) : GetCharacterDetailRepository{
    /**
     * This variable is getting the API from the API Interface class called RemoteGeneralDataStore
     * and Retrofit calls builder inside ServerInfo class
     */
    private val serverRestAPI by lazy {getEndpointGenerator()}

    private fun getEndpointGenerator(): RemoteGeneralDatastore {
        return ServerInfo.create(ServerInfo.GENERAL_API_URL, RemoteGeneralDatastore::class.java)
    }

    /**
     * Do observable with data "true" and do concatMap with the expected API
     */
    override fun getCharacterDetail(id: Int): Observable<GetCharacterDetailResponse> {
        return Observable.just(true)
            .concatMap { serverRestAPI.getCharacterDetail(id) }
            .subscribeOn(ioScheduler)
            .observeOn(compScheduler)
    }
}
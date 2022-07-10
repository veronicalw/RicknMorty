package com.leony.app.ricknmorty.data.repository

import android.content.Context
import com.leony.app.ricknmorty.data.helper.ServerInfo
import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import com.leony.app.ricknmorty.data.retrofit.RemoteGeneralDatastore
import io.reactivex.Observable
import io.reactivex.Scheduler

class GetCharacterListRepositoryImpl (val context: Context, val ioScheduler: Scheduler, val  compScheduler: Scheduler) : GetCharacterListRepository {
    private val serverRestAPI by lazy {getEndpointGenerator()}

    private fun getEndpointGenerator(): RemoteGeneralDatastore {
        return ServerInfo.create(ServerInfo.GENERAL_API_URL, RemoteGeneralDatastore::class.java)
    }

    override fun getCharacterList(): Observable<GetCharacterListResponse> {
        return Observable.just(true)
            .concatMap { serverRestAPI.getCharacterList() }
            .subscribeOn(ioScheduler)
            .observeOn(compScheduler)
    }
}
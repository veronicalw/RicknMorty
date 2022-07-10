package com.leony.app.ricknmorty.data.helper

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServerInfo {
    companion object {
        const val GENERAL_API_URL = "https://rickandmortyapi.com"

        private val rxJava2CallAdapterFactory =
            RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

        fun <S> create(serverUrl: String, clazz: Class<S>): S {
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(rxJava2CallAdapterFactory)

            return retrofitBuilder.build().create(clazz)
        }
    }
}
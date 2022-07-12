package com.leony.app.ricknmorty.data.helper

import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ServerInfo {
    companion object {
        /**
         * "A Host" or general url of the API
         */
        const val GENERAL_API_URL = "https://rickandmortyapi.com"

        /**
         * Because this project uses RxJava2, the adapter used when building
         * the retrofit call is the adapter provided by RxJava2.
         */
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
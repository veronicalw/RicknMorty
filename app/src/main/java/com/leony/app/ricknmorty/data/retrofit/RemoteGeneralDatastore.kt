package com.leony.app.ricknmorty.data.retrofit

import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse
import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import io.reactivex.Observable
import retrofit2.http.*

interface RemoteGeneralDatastore {

    companion object {
        const val API_CHARACTER = "/api/character"
    }

    /**
     * APIs for accessing Rick and Morty Datas
     */
    @GET(API_CHARACTER)
    fun getCharacterList(): Observable<GetCharacterListResponse>

    @GET("$API_CHARACTER/{id}")
    fun getCharacterDetail(
        @Path("id") characterId: Int
    ): Observable<GetCharacterDetailResponse>
}
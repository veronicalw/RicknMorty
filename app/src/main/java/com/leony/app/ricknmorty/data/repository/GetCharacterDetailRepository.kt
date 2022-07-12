package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse
import io.reactivex.Observable

interface GetCharacterDetailRepository {
    /**
     * Method Observable for requesting character's detail that returns GetCharacterDetailResponse
     */
    fun getCharacterDetail(id: Int): Observable<GetCharacterDetailResponse>
}
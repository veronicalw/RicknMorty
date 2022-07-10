package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse
import io.reactivex.Observable

interface GetCharacterDetailRepository {
    fun getCharacterDetail(id: Int): Observable<GetCharacterDetailResponse>
}
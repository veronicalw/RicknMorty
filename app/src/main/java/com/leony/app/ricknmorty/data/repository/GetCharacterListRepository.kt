package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import io.reactivex.Observable

interface GetCharacterListRepository {
    fun getCharacterList(): Observable<GetCharacterListResponse>
}
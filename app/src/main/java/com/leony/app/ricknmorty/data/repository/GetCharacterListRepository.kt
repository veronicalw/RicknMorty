package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import io.reactivex.Observable

interface GetCharacterListRepository {
    /**
     * Method Observable for requesting character's list that returns GetCharacterListResponse
     */
    fun getCharacterList(): Observable<GetCharacterListResponse>
}
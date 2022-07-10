package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterListResponse

interface GetCharacterListView {
    fun displayCharacterListResult(response: GetCharacterListResponse)
    fun displayCharacterListError(error: String)
}
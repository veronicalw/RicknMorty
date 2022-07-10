package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse

interface GetCharacterDetailView {
    fun displayCharacterDetailResult(response: GetCharacterDetailResponse)
    fun displayCharacterDetailError(error: String)
}
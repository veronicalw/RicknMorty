package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterDetailResponse

/**
 * Repository class to handle the UI whenever the request has been done by the worker class.
 * (GetCharacterDetailWorker)
 */
interface GetCharacterDetailView {
    fun displayCharacterDetailResult(response: GetCharacterDetailResponse)
    fun displayCharacterDetailError(error: String)
}
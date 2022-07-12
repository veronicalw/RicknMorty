package com.leony.app.ricknmorty.data.repository

import com.leony.app.ricknmorty.data.model.GetCharacterListResponse

/**
 * Repository class to handle the UI whenever the request has been done by the worker class.
 * (GetCharacterListWorker)
 */
interface GetCharacterListView {
    fun displayCharacterListResult(response: GetCharacterListResponse)
    fun displayCharacterListError(error: String)
}
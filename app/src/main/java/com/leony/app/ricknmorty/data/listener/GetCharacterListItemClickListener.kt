package com.leony.app.ricknmorty.data.listener

interface GetCharacterListItemClickListener {
    /**
     * onClick Listener for the Character's RecyclerView to pass the ID so that we can request for
     * character's details
     */
    fun onClickedCharacterId(id: Int)
}
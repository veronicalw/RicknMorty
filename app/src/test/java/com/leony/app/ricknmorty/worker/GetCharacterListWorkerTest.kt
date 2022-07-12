package com.leony.app.ricknmorty.worker

import android.content.Context
import android.content.res.Resources
import com.leony.app.ricknmorty.data.helper.InternetConnectionHelper
import com.leony.app.ricknmorty.data.model.GetCharacterListResponse
import com.leony.app.ricknmorty.data.model.GetCharacterResults
import com.leony.app.ricknmorty.data.model.GetPagesInfo
import com.leony.app.ricknmorty.data.repository.GetCharacterListRepository
import com.leony.app.ricknmorty.data.repository.GetCharacterListView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*

class GetCharacterListWorkerTest {
    private lateinit var characterListWorker: GetCharacterListWorker
    private val characterListView = Mockito.mock(GetCharacterListView::class.java)
    private val characterListRepository = Mockito.mock(GetCharacterListRepository::class.java)
    private val context = Mockito.mock(Context::class.java)
    private val resources = Mockito.mock(Resources::class.java)
    private val internetConnectionHelper = InternetConnectionHelper(context)
    private val characterItemListOnTest = GetCharacterListResponse(
        GetPagesInfo("https://rickandmortyapi.com/api/character?page=2", prev=null),
        arrayListOf(
            GetCharacterResults(1, "Rick Sanchez", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/1.jpeg"),
            GetCharacterResults(2, "Morty Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/2.jpeg"),
            GetCharacterResults(3, "Summer Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/3.jpeg"),
            GetCharacterResults(4, "Beth Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/4.jpeg"),
            GetCharacterResults(5, "Jerry Smith", "Alive","Human","https://rickandmortyapi.com/api/character/avatar/5.jpeg"),
         )
    )

    @Before
    fun setup(){
        characterListWorker = GetCharacterListWorker(
            resources,
            characterListRepository,
            characterListView,
            internetConnectionHelper,
            Schedulers.trampoline()
        )
    }

    /**
     * is Network Not Connected
     */
    @Test
    fun testNetworkisNotConnected(){
        `when`(characterListRepository.getCharacterList()).thenReturn(
            Observable.just(characterItemListOnTest)
        )
        characterListWorker.execute()
        verify(characterListView, never()).displayCharacterListResult(characterItemListOnTest)
    }

    /**
     * is Character List Item Data Retrieved
     */
    @Test
    fun testExecuteShowCharacterListItem(){
        `when`(characterListRepository.getCharacterList()).thenReturn(
            Observable.just(characterItemListOnTest)
        )
        characterListWorker.handleRequestCharacterList()

        val captor: KArgumentCaptor<GetCharacterListResponse> = argumentCaptor()
        verify(characterListView).displayCharacterListResult(captor.capture())

        assertEquals(captor.lastValue, characterItemListOnTest)
    }
}
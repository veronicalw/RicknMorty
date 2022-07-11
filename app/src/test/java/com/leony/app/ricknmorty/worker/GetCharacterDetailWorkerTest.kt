package com.leony.app.ricknmorty.worker

import android.accounts.NetworkErrorException
import android.content.Context
import android.content.res.Resources
import com.leony.app.ricknmorty.data.helper.InternetConnectionHelper
import com.leony.app.ricknmorty.data.model.*
import com.leony.app.ricknmorty.data.repository.GetCharacterDetailRepository
import com.leony.app.ricknmorty.data.repository.GetCharacterDetailView
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.kotlin.*

class GetCharacterDetailWorkerTest {
    private lateinit var characterDetailWorker: GetCharacterDetailWorker
    private val characterDetailView = Mockito.mock(GetCharacterDetailView::class.java)
    private val characterDetailRepository = Mockito.mock(GetCharacterDetailRepository::class.java)
    private val context = Mockito.mock(Context::class.java)
    private val resources = Mockito.mock(Resources::class.java)
    private val internetConnectionHelper = InternetConnectionHelper(context)
    private val characterItemDetailOnTest = GetCharacterDetailResponse(
        1,
        "Rick Sanchez",
        "Alive",
        "Human",
        "",
        "Male",
        GetCharacterDetailOriginLocation("Earth (C-137)"),
        GetCharacterDetailOriginLocation("Citadel of Ricks"),
        "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        arrayListOf()
    )

    @Before
    fun setup(){
        characterDetailWorker = GetCharacterDetailWorker(
            resources,
            characterDetailRepository,
            characterDetailView,
            internetConnectionHelper,
            Schedulers.trampoline())
    }

    /**
     * is Network Not Connected
     */
    @Test
    fun testNetworkisNotConnected(){
        given(characterDetailRepository.getCharacterDetail(1)).willReturn(
            Observable.error(NetworkErrorException())
        )
        characterDetailWorker.execute(1)
        verify(characterDetailView, never()).displayCharacterDetailResult(characterItemDetailOnTest)
    }


    /**
     * is Character Item Detail Data Retrieved
     */
    @Test
    fun testExecuteShowCharacterDetail() {
        `when`(characterItemDetailOnTest.id?.let { characterDetailRepository.getCharacterDetail(it) })
            .thenReturn(Observable.just(characterItemDetailOnTest))

        characterDetailWorker.handleRequestCharacterList(1)

        val captor: KArgumentCaptor<GetCharacterDetailResponse> = argumentCaptor()
        verify(characterDetailView).displayCharacterDetailResult(captor.capture())

        assertEquals(captor.lastValue, characterItemDetailOnTest)
    }
}
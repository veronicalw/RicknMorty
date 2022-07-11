package com.leony.app.ricknmorty.worker

import android.content.res.Resources
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.data.helper.InternetConnectionHelper
import com.leony.app.ricknmorty.data.repository.GetCharacterListRepository
import com.leony.app.ricknmorty.data.repository.GetCharacterListView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.net.ssl.SSLException

class GetCharacterListWorker(
    val resources: Resources,
    val repository: GetCharacterListRepository,
    val view: GetCharacterListView,
    val internetConnectionHelper: InternetConnectionHelper,
    val processScheduler: Scheduler) {

    private var defaultStateDisposable: Disposable? = null

    fun execute(){
        internetConnectionHelper.checkInternetConnection()
            .subscribe{
                if (it){
                    handleRequestCharacterList()
                } else {
                    view.displayCharacterListError(resources.getString(R.string.messageNetworkError))
                }
            }
    }

    fun handleRequestCharacterList(){
        defaultStateDisposable = Observable.just(true)
            .flatMap { repository.getCharacterList() }
            .subscribeOn(processScheduler)
            .subscribe({
                view.displayCharacterListResult(it)
            },
            {
                if (it is SSLException) {
                    view.displayCharacterListError(it.message.toString())
                } else {
                    view.displayCharacterListError(resources.getString(R.string.messageNetworkError))
                }
            }
            )
    }

    fun stopWorker(){
        defaultStateDisposable?.isDisposed
        defaultStateDisposable = null
    }
}
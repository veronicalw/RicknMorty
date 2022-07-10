package com.leony.app.ricknmorty.worker

import android.content.res.Resources
import com.leony.app.ricknmorty.R
import com.leony.app.ricknmorty.data.helper.InternetConnectionHelper
import com.leony.app.ricknmorty.data.repository.GetCharacterDetailRepository
import com.leony.app.ricknmorty.data.repository.GetCharacterDetailView
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import javax.net.ssl.SSLException

class GetCharacterDetailWorker (
    val resources: Resources,
    val repository: GetCharacterDetailRepository,
    val view: GetCharacterDetailView,
    val internetConnectionHelper: InternetConnectionHelper,
    val processScheduler: Scheduler
) {

    private var defaultStateDisposable: Disposable? = null

    fun execute(id: Int){
        internetConnectionHelper.checkInternetConnection()
            .subscribe{
                if (it){
                    handleRequestCharacterList(id)
                } else {
                    view.displayCharacterDetailError(resources.getString(R.string.messageNetworkError))
                }
            }
    }

    private fun handleRequestCharacterList(id: Int){
        defaultStateDisposable = Observable.just(true)
            .flatMap { repository.getCharacterDetail(id) }
            .subscribeOn(processScheduler)
            .subscribe({
                view.displayCharacterDetailResult(it)
            },
                {
                    if (it is SSLException) {
                        view.displayCharacterDetailError(it.message.toString())
                    } else {
                        view.displayCharacterDetailError(resources.getString(R.string.messageNetworkError))
                    }
                }
            )
    }

    fun stopWorker(){
        defaultStateDisposable?.isDisposed
        defaultStateDisposable = null
    }
}
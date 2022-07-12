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

/**
 * This is a worker class for handling requests and sending the response to the View all at once.
 * To implement a neat flow, usually, the worker will be divided into two parts of responsibility.
 * One will work for the needs of the request, and the other will work for sending the response it
 * gets to the view.
 *
 * Example of how it works:
 *
 * In Business Logic project: ActionType class, ActionCall class, Worker class, Data/Model Class
 * 1. ActionType class contain list of actions needed in a static final variable.
 * E.g: ActionType.ON_REQUEST_CHARACTER_DETAIL
 *
 * 2. ActionCall class contain class of actions with parameters of any objects.
 * E.g: class requestCharacterDetail(id: Int): Action(ActionType.ON_REQUEST_CHARACTER_DETAIL)
 *
 * 3. A Worker class is responsible for the execution of what type of action should be run after
 * the previous action
 * E.g: ActionType.ON_REQUEST_CHARACTER_DETAIL -> executeNextAction(Action(ActionType.REQUEST_CHARACTER_DETAIL))
 *
 * 4. A Model class is responsible for the standardized data mapping
 */
class GetCharacterDetailWorker (
    val resources: Resources,
    val repository: GetCharacterDetailRepository,
    val view: GetCharacterDetailView,
    val internetConnectionHelper: InternetConnectionHelper,
    val processScheduler: Scheduler
) {

    private var defaultStateDisposable: Disposable? = null

    /**
     * Execute the intended request (In this case, would be character details)
     */
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

    /**
     * Do observable with item data true and do flatmap with requesting the character detail
     * after getting the response, subscribe will listen and send the retrieved data to the view
     * through view repository class - GetCharacterDetailView
     */
    fun handleRequestCharacterList(id: Int){
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

    /**
     * Always stop worker and delete the disposable when it reached the execution or the user is not
     * in the expected activity anymore.
     */
    fun stopWorker(){
        defaultStateDisposable?.isDisposed
        defaultStateDisposable = null
    }
}
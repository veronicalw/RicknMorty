package com.leony.app.ricknmorty.data.helper

import android.content.Context
import android.net.ConnectivityManager
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.net.InetAddress
import io.reactivex.functions.Function
import javax.net.ssl.SSLException

class InternetConnectionHelper(val context: Context) {
    companion object {
        val DEFAULT_HOST = "www.google.com"
    }

    fun checkInternetConnection(): Observable<Boolean> {
        if(context != null){
            return checkInternetConnection(context, DEFAULT_HOST)
        }
        return Observable.just(false)
    }

    /**
     * Observe the internet connection, pass it to isActiveNetworkConnected(context) and map it to
     * isInternetAvailable(host)
     */
    private fun checkInternetConnection(context: Context, host: String): Observable<Boolean> {
        return Observable
            .create<Boolean?> { emitter ->
                emitter.onNext(isActiveNetworkConnected(context))
                emitter.onComplete()
            }
            .onErrorReturn {
                if (it is SSLException){
                    throw it
                } else {
                    false
                }
            }
            .map(Function<Boolean, Boolean> { hasNetwork ->
                if (hasNetwork) {
                    isInternetAvailable(host)
                } else {
                    hasNetwork
                }
            })
            .switchIfEmpty(Observable.just(java.lang.Boolean.FALSE))
            .subscribeOn(Schedulers.io())
    }

    /**
     * Monitor network connections
     */
    private fun isActiveNetworkConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return (networkInfo != null
                && networkInfo.isAvailable
                && networkInfo.isConnectedOrConnecting)
    }

    private fun isInternetAvailable(host: String): Boolean {
        return try {
            val inetAddress = InetAddress.getByName(host)
            !inetAddress.equals("")
        } catch (ex: Exception) {
            false
        }

    }
}
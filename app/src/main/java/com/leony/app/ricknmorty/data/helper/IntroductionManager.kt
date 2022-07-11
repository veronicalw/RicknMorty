package com.leony.app.ricknmorty.data.helper

import android.content.Context
import android.content.SharedPreferences

class IntroductionManager(context: Context) {
    companion object{
        const val INTRO_PREFERENCE_KEY = "intro_preference_key"
        const val INTRO_PREFERENCE_VALUE = "intro_preference_value"
    }

    private var preferences: SharedPreferences = context.getSharedPreferences(INTRO_PREFERENCE_KEY, Context.MODE_PRIVATE)
    private var editor: SharedPreferences.Editor = preferences.edit()

    fun setFirstTimeView(isFirst: Boolean){
        editor.putBoolean(INTRO_PREFERENCE_VALUE, isFirst)
        editor.apply()
    }

    fun checkIsFirstTime(): Boolean {
        return preferences.getBoolean(INTRO_PREFERENCE_VALUE, false)
    }
}
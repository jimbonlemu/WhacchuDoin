package com.jimbonlemu.whacchudoin.utils

import android.content.Context
import android.content.SharedPreferences
import com.jimbonlemu.whacchudoin.data.network.auth.LoginResult

class PreferenceManager(context: Context) {
    private var prefs: SharedPreferences =
        context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    val getToken = prefs.getString(KEY_TOKEN, "")
    val getName = prefs.getString(KEY_NAME, "")

    fun setLoginPrefs(loginResult: LoginResult) {
        editor.putString(KEY_NAME, loginResult.name)
        editor.putString(KEY_TOKEN, loginResult.token)
        editor.apply()
    }

    fun clearAllPreferences() {
        editor.remove(KEY_TOKEN)
        editor.remove(KEY_NAME)
        editor.apply()
    }
}
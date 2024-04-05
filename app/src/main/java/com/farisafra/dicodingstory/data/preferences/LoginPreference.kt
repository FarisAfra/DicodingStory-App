package com.farisafra.dicodingstory.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.farisafra.dicodingstory.data.response.login.LoginResultResponse

class LoginPreference(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "login_prefs"
        private const val KEY_NAME = "name"
        private const val KEY_USER_ID = "user_id"
        private const val KEY_TOKEN = "token"
    }

    fun setLogin(name: String, userId: String, token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_NAME, name)
        editor.putString(KEY_USER_ID, userId)
        editor.putString(KEY_TOKEN, token)
        editor.apply()
    }

    fun getLogin(): LoginResultResponse {
        val name = sharedPreferences.getString(KEY_NAME, "") ?: ""
        val userId = sharedPreferences.getString(KEY_USER_ID, "") ?: ""
        val token = sharedPreferences.getString(KEY_TOKEN, "") ?: ""
        return LoginResultResponse(name, userId, token)
    }

    fun getToken(): String? {
        return sharedPreferences.getString(KEY_TOKEN, null)
    }

    fun clearLogin() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_NAME)
        editor.remove(KEY_USER_ID)
        editor.remove(KEY_TOKEN)
        editor.apply()
    }
}
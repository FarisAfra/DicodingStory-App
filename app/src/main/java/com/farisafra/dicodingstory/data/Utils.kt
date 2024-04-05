package com.farisafra.dicodingstory.data

import android.content.Context

object Utils {
    fun isUserLoggedIn(context: Context): Boolean {
        val sharedPreferences = context.getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        val token = sharedPreferences.getString("token", null)
        return !token.isNullOrEmpty()
    }
}
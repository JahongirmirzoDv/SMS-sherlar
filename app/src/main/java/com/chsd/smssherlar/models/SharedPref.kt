package com.chsd.smssherlar.models

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

object SharedPref {
    lateinit var sharedPreferences: SharedPreferences
    fun getContext(context: Context) {
        sharedPreferences = context.getSharedPreferences("list", Context.MODE_PRIVATE)
    }

    var sms: String?
        get() = sharedPreferences.getString("name", null)
        set(value) = SharedPref.sharedPreferences.edit() {
            if (value != null) {
                this.putString("name", value)
            }
        }
    var like: String?
        get() = sharedPreferences.getString("like", null)
        set(value) = SharedPref.sharedPreferences.edit() {
            if (value != null) {
                this.putString("like", value)
            }
        }
}
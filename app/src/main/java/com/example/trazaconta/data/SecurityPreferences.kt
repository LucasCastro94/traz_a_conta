package com.example.trazaconta.data

import android.content.Context

class SecurityPreferences(val context: Context){

    private val mSharedPreferences = context.getSharedPreferences("app", Context.MODE_PRIVATE)


    fun saveFK(key: String, value: Long)
    {
        mSharedPreferences.edit().putLong(key,value).apply()
    }

    fun getFK(key: String) : Long
    {
        return mSharedPreferences.getLong(key,0L)
    }

}
package com.kirkyweather.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

// 获取全局Context对象
class KirkyWeatherApplication: Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        const val TOKEN = "cIecnVlovchAFYIk"
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}
package com.kirkyweather.android.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


// 1. 使用了Retrofit.Builder来构建一个Retrofit对象
// 2. 调用它的create()方法，并传入具体Service接口所对应的Class类型，创建一个该接口的动态代理对象
object ServiceCreator {
    private const val BASE_URL = "https://api.caiyunapp.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
    inline fun <reified T> create(): T {
        return create(T::class.java)
    }
}

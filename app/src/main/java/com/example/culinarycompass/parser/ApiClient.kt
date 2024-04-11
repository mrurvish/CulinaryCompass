package com.example.culinarycompass.parser

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private val CONNECTION_TIMEOUT = 30 //seconds

    private val READ_TIMEOUT = 20 //seconds

    private val WRITE_TIMEOUT = 20
    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS).readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS).writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .build()
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.edamam.com/")
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)
}

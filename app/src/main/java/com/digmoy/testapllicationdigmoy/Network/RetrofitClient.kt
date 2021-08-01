package com.digmoy.testapllicationdigmoy.Network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val URL = "https://jsonplaceholder.typicode.com/"

    private val retrofitClient: Retrofit.Builder by lazy {

        val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

//        val headerInterceptor = Interceptor { chain ->
//            var request = chain.request()
//            request = request.newBuilder().addHeader("X-API-KEY","EDUCATION@123").build()
//
//            val response = chain.proceed(request)
//
//            response
//        }

        val okHttp: OkHttpClient.Builder = OkHttpClient.Builder()
            //.addInterceptor(headerInterceptor)
            .addInterceptor(logger)

        Retrofit.Builder()
            .baseUrl(URL)
            .client(okHttp.build())
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: ApiInterface by lazy {
        retrofitClient
            .build()
            .create(ApiInterface::class.java)
    }
}
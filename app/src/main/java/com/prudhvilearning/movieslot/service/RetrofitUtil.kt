package com.prudhvilearning.movieslot.service

import com.google.gson.GsonBuilder
import com.prudhvilearning.movieslot.ui.interfaces.ApiInterface
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.prudhvilearning.movieslot.BuildConfig
import com.prudhvilearning.movieslot.constants.ApiEndpoint.Companion.BASE_URL
import java.util.concurrent.TimeUnit

val networkModule = module {

    // OkHttpClient with Logging for Debug
    single {
        val builder = OkHttpClient.Builder()
            .connectTimeout(70, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            builder.addInterceptor(logging)
        }

        builder.build()
    }

    // Retrofit instance
    single {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(get()) // uses OkHttpClient
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
    }

    // API Interface
    single<ApiInterface> {
        get<Retrofit>().create(ApiInterface::class.java)
    }
}
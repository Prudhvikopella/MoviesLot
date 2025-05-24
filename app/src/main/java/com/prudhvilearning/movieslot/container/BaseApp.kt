package com.prudhvilearning.movieslot.container

import android.app.Application
import com.prudhvilearning.movieslot.koin.repoModule
import com.prudhvilearning.movieslot.koin.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BaseApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BaseApp)
            modules(
                listOf(
                    com.prudhvilearning.movieslot.service.networkModule,
                    com.prudhvilearning.movieslot.koin.repoModule,
                    com.prudhvilearning.movieslot.koin.viewModelModule
                )
            )
        }
    }
}
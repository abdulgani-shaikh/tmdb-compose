package com.shaikhabdulgani.tmdb.global

import android.app.Application
import com.shaikhabdulgani.tmdb.di.appModule
import com.shaikhabdulgani.tmdb.di.databaseModule
import com.shaikhabdulgani.tmdb.di.networkModule
import com.shaikhabdulgani.tmdb.di.repositoryModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin


class App : Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                databaseModule,
                networkModule,
                repositoryModule
            )
        }
    }
}
package com.pratama.kmmcurrency.android

import android.app.Application
import com.pratama.kmmcurrency.android.di.vmModule
import com.pratama.kmmcurrency.core.CoreApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        CoreApp.initialize {
            androidContext(applicationContext)
            androidLogger()
            modules(listOf(vmModule)) // list di for android module
        }

    }
}
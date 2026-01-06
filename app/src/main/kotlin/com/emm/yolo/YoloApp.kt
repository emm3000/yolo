package com.emm.yolo

import android.app.Application
import com.emm.yolo.presentation.share.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class YoloApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@YoloApp)
            modules(mainModule)
        }
    }
}
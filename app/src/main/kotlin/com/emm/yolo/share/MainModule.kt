package com.emm.yolo.share

import com.emm.yolo.EmmDatabaseData
import com.emm.yolo.data.LogRepository
import com.emm.yolo.data.provideSqlDriver
import com.emm.yolo.feature.history.PracticeHistoryViewModel
import com.emm.yolo.feature.home.EnglishHomeViewModel
import com.emm.yolo.feature.log.AudioPlayer
import com.emm.yolo.feature.log.AudioRecordMachine
import com.emm.yolo.feature.log.AudioRecordMachineImpl
import com.emm.yolo.feature.log.LogEnglishSessionViewModel
import com.emm.yolo.feature.progress.ProgressInsightsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule = module {

    single<EmmDatabaseData> { provideSqlDriver(androidContext()) }
    viewModelOf(::LogEnglishSessionViewModel)
    viewModelOf(::EnglishHomeViewModel)
    viewModelOf(::PracticeHistoryViewModel)
    viewModelOf(::ProgressInsightsViewModel)

    singleOf(::LogRepository)

    factory {
        AudioRecordMachineImpl(
            context = androidContext()
        )
    } bind AudioRecordMachine::class

    factory { AudioPlayer.Factory() }
}
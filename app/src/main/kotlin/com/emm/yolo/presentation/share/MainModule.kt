package com.emm.yolo.presentation.share

import com.emm.yolo.EmmDatabaseData
import com.emm.yolo.data.Repository
import com.emm.yolo.presentation.feature.history.PracticeHistoryViewModel
import com.emm.yolo.presentation.feature.home.EnglishHomeViewModel
import com.emm.yolo.presentation.feature.log.LogEnglishSessionViewModel
import com.emm.yolo.presentation.feature.log.recorder.AudioRecordMachine
import com.emm.yolo.presentation.feature.log.recorder.AudioRecordMachineImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val mainModule = module {

    single<EmmDatabaseData> { provideSqlDriver(androidContext()) }
    viewModel {
        LogEnglishSessionViewModel(
            repository = get(),
            audioRecordMachine = get(),
            application = androidApplication()
        )
    }
    viewModelOf(::EnglishHomeViewModel)
    viewModelOf(::PracticeHistoryViewModel)

    singleOf(::Repository)

    factory {
        AudioRecordMachineImpl(
            context = androidContext()
        )
    } bind AudioRecordMachine::class
}
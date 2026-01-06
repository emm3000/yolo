package com.emm.yolo.presentation.share

import com.emm.yolo.EmmDatabaseData
import com.emm.yolo.data.Repository
import com.emm.yolo.presentation.feature.log.LogEnglishSessionViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val mainModule = module {

    single<EmmDatabaseData> { provideSqlDriver(androidContext()) }
    viewModelOf(::LogEnglishSessionViewModel)

    singleOf(::Repository)
}
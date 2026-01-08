package com.emm.yolo.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.emm.yolo.BuildConfig
import com.emm.yolo.EmmDatabaseData

fun provideSqlDriver(context: Context): EmmDatabaseData {
    val driver: SqlDriver = AndroidSqliteDriver(
        schema = EmmDatabaseData.Schema,
        context = context,
        name = "${BuildConfig.APPLICATION_ID}.db",
        callback = csm()
    )
    return provideDb(driver)
}

fun csm(): AndroidSqliteDriver.Callback = object : AndroidSqliteDriver.Callback(
    schema = EmmDatabaseData.Schema,
) {
    override fun onOpen(db: SupportSQLiteDatabase) {
        db.setForeignKeyConstraintsEnabled(true)
    }
}

fun provideDb(sqlDriver: SqlDriver): EmmDatabaseData = EmmDatabaseData(sqlDriver)
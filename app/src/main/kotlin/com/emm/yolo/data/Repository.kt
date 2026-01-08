package com.emm.yolo.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.emm.yolo.EmmDatabaseData
import com.emm.yolo.EnglishSession
import com.emm.yolo.presentation.feature.log.InsertSession
import com.emm.yolo.presentation.share.currentTimeInMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class Repository(db: EmmDatabaseData) {

    private val sessionDao = db.englishSessionQueries
    private val audioDao = db.audioPracticeQueries

    suspend fun insertSession(insertSession: InsertSession): Long = withContext(Dispatchers.IO) {
        sessionDao.insertSession(
            sessionDate = insertSession.sessionDate,
            sessionHour = insertSession.sessionHour,
            minutesPracticed = insertSession.minutesPracticed.minutes,
            practiceType = insertSession.practiceType.name,
            confidenceLevel = insertSession.confidenceLevel,
            discomfortLevel = insertSession.discomfortLevel,
            notes = insertSession.notes,
            createdAt = currentTimeInMillis(),
            updatedAt = currentTimeInMillis(),
        )
        val executeAsOne: EnglishSession = sessionDao.selectByDate(insertSession.sessionDate)
            .executeAsOne()
        return@withContext executeAsOne.id
    }

    suspend fun insertAudio(
        sessionId: Long,
        filePath: String,
        durationSeconds: Long,
        prompt: String?
    ) = withContext(Dispatchers.IO) {
        audioDao.insertAudioPractice(
            sessionId = sessionId,
            filePath = filePath,
            durationSeconds = durationSeconds,
            prompt = prompt,
            createdAt = currentTimeInMillis(),
        )
    }

    fun selectAllSessions(): Flow<List<EnglishSession>> {
        return sessionDao.selectAll()
            .asFlow()
            .mapToList(Dispatchers.IO)
    }
}
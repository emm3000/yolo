package com.emm.yolo.data

import com.emm.yolo.EmmDatabaseData
import com.emm.yolo.presentation.feature.log.InsertSession
import com.emm.yolo.presentation.share.currentTimeInMillis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(db: EmmDatabaseData) {

    private val sessionDao = db.englishSessionQueries

    suspend fun insertSession(insertSession: InsertSession) = withContext(Dispatchers.IO) {
        sessionDao.insertSession(
            session_date = insertSession.sessionDate,
            minutes_practiced = insertSession.minutesPracticed.minutes,
            practice_type = insertSession.practiceType.name,
            confidence_level = insertSession.confidenceLevel,
            discomfort_level = insertSession.discomfortLevel,
            notes = insertSession.notes,
            created_at = currentTimeInMillis(),
            updated_at = currentTimeInMillis(),
        )
    }
}
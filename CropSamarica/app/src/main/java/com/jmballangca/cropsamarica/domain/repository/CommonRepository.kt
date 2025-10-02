package com.jmballangca.cropsamarica.domain.repository

import com.jmballangca.cropsamarica.domain.models.Developers
import com.jmballangca.cropsamarica.domain.models.Notifications
import com.jmballangca.cropsamarica.domain.models.UserGuide
import kotlinx.coroutines.flow.Flow


interface CommonRepository {
   suspend fun getAllDevelopers() : List<Developers>

   suspend fun getAllUserGuides() : List<UserGuide>

   fun getAllMyNotifications(
      uid : String
   ) : Flow<List<Notifications>>


   suspend fun getNotificationById(
      id : String
   ) : Result<Notifications?>
   suspend fun updateNotificationStatus(
      id : String,
   )

   suspend fun deleteNotification(
      id : String
   ) : Result<String>
}
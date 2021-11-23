package com.sideproject.petch

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveMonitoringUpdate
import androidx.health.services.client.data.UserActivityState

class BackgroundDataReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Check that the Intent is for passive data
        if (intent?.action != PassiveMonitoringUpdate.ACTION_DATA) {
            return
        }
        val update = PassiveMonitoringUpdate.fromIntent(intent) ?: return

        // List of available data points
        val dataPoints = update.dataPoints

        // List of available user state info
        val userActivityInfoList = update.userActivityInfoUpdates

        Log.d(LOG_TAG, "data Points : $dataPoints")

        Log.d(LOG_TAG, "user Activity Info List : $userActivityInfoList")

        // Inspect the last reported state change, if present
        PassiveMonitoringUpdate(dataPoints, userActivityInfoList).userActivityInfoUpdates.lastOrNull()?.let { userActivityInfo ->
            // When the transition to this state took place
            val stateChangeInstant = userActivityInfo.stateChangeTime

            // The high-level state of the user, e.g. USER_ACTIVITY_ASLEEP, USER_ACTIVITY_EXERCISE
            val state = userActivityInfo.userActivityState

            if (state == UserActivityState.USER_ACTIVITY_EXERCISE) {
                // Obtain info about the exercise and whether it is owned by the app
                val exerciseInfo = userActivityInfo.exerciseInfo
                Log.d(LOG_TAG, "exerciseInfo : $exerciseInfo")
            }

            Log.d(LOG_TAG, "stateChangeInstant = $stateChangeInstant")
            Log.d(LOG_TAG, "state : $state")
        }
    }

    companion object{
        private val LOG_TAG = BackgroundDataReceiver::class.java.simpleName
    }
}
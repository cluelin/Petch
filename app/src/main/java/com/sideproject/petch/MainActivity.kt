package com.sideproject.petch

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.concurrent.futures.await
import androidx.health.services.client.ExerciseUpdateListener
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.*
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
//        setAmbientEnabled()


        checkCapability()




    }

    private var supportsHeartRate : Boolean = false

    private var supportsStepsGoal : Boolean = false

    private var supportsWalk : Boolean = false

    fun checkCapability(){

        Log.d("tag", "checkCapability invoked")

        val healthClient = HealthServices.getClient(this)
        val passiveMonitoringClient = healthClient.passiveMonitoringClient
        lifecycleScope.launchWhenCreated {
            val capabilities = passiveMonitoringClient.capabilities.await()
            // Supported types for passive data collection
            supportsHeartRate =
                DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
            // Supported types for PassiveGoals
            supportsStepsGoal =
                DataType.STEPS in capabilities.supportedDataTypesEvents

            supportsWalk = DataType.WALKING_STEPS in capabilities.supportedDataTypesEvents

            Log.d("tag", "supportsHeartRate : $supportsHeartRate")
        }

    }




g
    val listener = object : ExerciseUpdateListener {
        override fun onExerciseUpdate(update: ExerciseUpdate) {

            Log.d("injag.jang", "onExerciseUpdate invoked")
            // Process the latest information about the exercise.
            var exerciseStatus = update.state // e.g. ACTIVE, USER_PAUSED, etc.
            var activeDuration = update.activeDuration // Duration
            var latestMetrics = update.latestMetrics // Map<DataType, List<DataPoint>>
            var latestAggregateMetrics =
                update.latestAggregateMetrics // Map<DataType, AggregateDataPoint>
            var latestGoals = update.latestAchievedGoals // Set<AchievedExerciseGoal>
            var latestMilestones =
                update.latestMilestoneMarkerSummaries // Set<MilestoneMarkerSummary>

        }

        override fun onLapSummary(lapSummary: ExerciseLapSummary) {
            // For ExerciseTypes that support laps, this is called when a lap is marked.
            Log.d("injae.jang", "onLapSummary invoked")
        }

        override fun onAvailabilityChanged(dataType: DataType, availability: Availability) {

            Log.d("injae.jang", "onAvailabilityChanged invoked")
            // Called when the availability of a particular DataType changes.
            when {
                availability is LocationAvailability -> {
                    // Relates to Location / GPS
                }
                availability is DataTypeAvailability ->{
                    // Relates to another DataType
                }
            }
        }
    }






}
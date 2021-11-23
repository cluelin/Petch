package com.sideproject.petch


import android.Manifest
import android.content.ComponentName
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.health.services.client.HealthServices
import androidx.health.services.client.data.DataType
import androidx.health.services.client.data.PassiveMonitoringConfig
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var permissionLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val result = checkPermission()

        Log.d(LOG_TAG, "result : $result")





    }

    private fun checkPermission() : Boolean{
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
                when (result) {
                    true -> {
                        Log.i(LOG_TAG, "Body sensors permission granted")
                        checkCapability()
                        registerReceiver()
                    }
                    false -> {
                        Log.i(LOG_TAG, "Body sensors permission not granted")
                    }
                }
            }


        when (ContextCompat.checkSelfPermission(applicationContext, PERMISSION_BODY_SENSORS)) {
            PackageManager.PERMISSION_GRANTED -> {
                checkCapability()
                registerReceiver()
                return true
            }

            else -> {
                permissionLauncher.launch(PERMISSION_BODY_SENSORS)
            }
        }

        when (ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.ACTIVITY_RECOGNITION)) {
            PackageManager.PERMISSION_GRANTED -> {
                checkCapability()
                registerReceiver()
                return true
            }

            else -> {
                permissionLauncher.launch(PERMISSION_BODY_SENSORS)
            }
        }

        return false
    }

    private var supportsHeartRate : Boolean = false

    private var supportsStepsGoal : Boolean = false

    fun checkCapability(){

        Log.d("tag", "checkCapability invoked")

        val healthClient = HealthServices.getClient(this)
        val passiveMonitoringClient = healthClient.passiveMonitoringClient
        lifecycleScope.launchWhenCreated {
            val capabilities = passiveMonitoringClient.capabilities.await()

            Log.d(LOG_TAG, "capabilities : $capabilities")
            // Supported types for passive data collection
            supportsHeartRate =
                DataType.HEART_RATE_BPM in capabilities.supportedDataTypesPassiveMonitoring
            // Supported types for PassiveGoals
            supportsStepsGoal =
                DataType.STEPS in capabilities.supportedDataTypesEvents

            val supportsStep =
                DataType.RUNNING_STEPS in capabilities.supportedDataTypesPassiveMonitoring

            val supportsWalk = DataType.WALKING_STEPS in capabilities.supportedDataTypesEvents

            Log.d("tag", "capabilities : $capabilities")
            Log.d("tag", "supportsHeartRate : $supportsHeartRate")
            Log.d("tag", "supportsStepsGoal : $supportsStepsGoal")
            Log.d("tag", "supportsStep : $supportsStep")
            Log.d("tag", "supportsWalk : $supportsWalk")
        }

    }

    fun registerReceiver(){
        var mContext = this


        val dataTypes = setOf(DataType.HEART_RATE_BPM, DataType.STEPS, DataType.RUNNING_STEPS, DataType.WALKING_STEPS)
        val config = PassiveMonitoringConfig.builder()
            .setDataTypes(dataTypes)
            .setComponentName(ComponentName(mContext, BackgroundDataReceiver::class.java))
            // To receive UserActivityState updates, ACTIVITY_RECOGNITION permission is required.
//            .setShouldIncludeUserActivityState(true)
            .build()

        lifecycleScope.launch {

            HealthServices.getClient(mContext)
                .passiveMonitoringClient
                .registerDataCallback(config)
                .await()
        }
    }

    companion object{
        private val LOG_TAG = MainActivity::class.java.simpleName

        private const val PERMISSION_BODY_SENSORS = "android.permission.BODY_SENSORS"
    }

}
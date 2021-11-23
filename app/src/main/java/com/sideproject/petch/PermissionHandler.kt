package com.sideproject.petch

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

class PermissionHandler (private var context: Context){


    fun handlePermission(activity : Activity){

//        when(ContextCompat.checkSelfPermission(context,PERMISSION_BODY_SENSORS)) {
//            PackageManager.PERMISSION_GRANTED -> {
//                // You can use the API that requires the permission.
//            }
//
//            else -> {
//                requestPermissions(activity,
//                    arrayOf(PERMISSION_BODY_SENSORS),
//                    REQUEST_CODE)
//
//            }
//        }

    }

    companion object{
        private val LOG_TAG = PermissionHandler::class.java.simpleName

        private const val PERMISSION_BODY_SENSORS = "android.permission.BODY_SENSORS"
    }
}
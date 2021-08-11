package com.example.another_location_plugin

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import io.flutter.plugin.common.PluginRegistry
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.location.LocationRequest.create

class AnotherLocationPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    PluginRegistry.ActivityResultListener {
    private lateinit var channel: MethodChannel
    private lateinit var context: Context
    private lateinit var activity: Activity

    private lateinit var permissionEventsEmitter: EventChannel
    private lateinit var permissionEventsSource: EventChannel.EventSink
    private var permissionEventsSourceHandler: EventChannel.StreamHandler =
        object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                permissionEventsSource = events
            }

            override fun onCancel(arguments: Any?) {

            }
        }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var locationEventsEmitter: EventChannel
    private lateinit var locationEventsSource: EventChannel.EventSink
    private var locationEventsSourceHandler: EventChannel.StreamHandler =
        object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink) {
                locationEventsSource = events
            }

            override fun onCancel(arguments: Any?) {
            }
        }

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, PLUGIN_CHANNEL_NAME)
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext

        permissionEventsEmitter =
            EventChannel(flutterPluginBinding.binaryMessenger, PERMISSION_CHANNEL_NAME)
        permissionEventsEmitter.setStreamHandler(permissionEventsSourceHandler)

        locationEventsEmitter =
            EventChannel(flutterPluginBinding.binaryMessenger, LOCATION_CHANNEL_NAME)
        locationEventsEmitter.setStreamHandler(locationEventsSourceHandler)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            GET_PLATFORM_VERSION -> getPlatformVersion(result)
            CHECK_PERMISSIONS -> checkPermissions()
            REQUEST_PERMISSIONS -> requestPermissions()
            INITIALIZE_PLUGIN -> initializePlugin()
            GET_LOCATION -> getLocation()
            STOP_PLUGIN -> abortGetLocation()
            else -> result.notImplemented()
        }
    }

    private fun initializePlugin() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
        val hasPermissions = checkPermissions()
        if (hasPermissions) {
            locationRequest = create().apply {
                interval = INTERVAL
                priority = PRIORITY_HIGH_ACCURACY
                locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult?) {
                        locationEventsSource.success(
                            result?.convertToString()
                        )
                    }
                }
            }
        } else {
            locationEventsSource.success(LOCATION_ERROR)
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        Log.i("onDetachFromAct(config)", "Not implemented")
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        Log.i("onReattachedToActivity", "Not implemented")
    }

    override fun onDetachedFromActivity() {
        Log.i("onDetachedFromActivity", "Not implemented")
    }

    override fun onActivityResult(REQUEST_CODE: Int, resultCode: Int, data: Intent?): Boolean {
        Log.i("onActivityResult", "Not implemented")
        return true
    }

    private fun getPlatformVersion(result: Result) {
        result.success("Android ${android.os.Build.VERSION.RELEASE}")
    }

    private fun checkPermissions(): Boolean {
        var allowed = false
        context?.run {
            allowed = ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
            permissionEventsSource?.success(
                if (allowed) {
                    PERMISSION_ALLOWED
                } else {
                    PERMISSION_NOT_ALLOWED
                }
            )
        }
        return allowed
    }

    private fun requestPermissions() {
        context?.run {
            activity?.apply {
                requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ),
                    REQUEST_CODE
                )
            }
        }
    }

    private fun getLocation() {
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun abortGetLocation() {
        context.run {
            fusedLocationProviderClient.removeLocationUpdates(
                locationCallback
            )
        }
        locationEventsSource?.success(
            STOP_LISTENING_MESSAGE
        )
    }

    companion object Constants {
        const val PLUGIN_CHANNEL_NAME = "another_location_plugin"
        const val PERMISSION_CHANNEL_NAME = "permission_event_channel"
        const val LOCATION_CHANNEL_NAME = "location_event_channel"
        const val INITIALIZE_PLUGIN = "initializePlugin"
        const val GET_PLATFORM_VERSION = "getPlatformVersion"
        const val CHECK_PERMISSIONS = "checkPermissions"
        const val REQUEST_PERMISSIONS = "requestPermissions"
        const val GET_LOCATION = "getLastLocation"
        const val STOP_PLUGIN = "stopPlugin"
        const val PERMISSION_ALLOWED = "Permission allowed"
        const val PERMISSION_NOT_ALLOWED = "Permission not allowed"
        const val REQUEST_CODE = 500
        const val INTERVAL: Long = 5000
        const val LONGITUDE_TAG = "Long:"
        const val LATITUDE_TAG = "Lat:"
        const val LOCATION_ERROR = "Permissions weren't allowed"
        const val STOP_LISTENING_MESSAGE = "You paused the location"
    }
}

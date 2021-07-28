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

class AnotherLocationPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
    PluginRegistry.ActivityResultListener {
    private lateinit var channel: MethodChannel

    private lateinit var permissionEventsEmitter: EventChannel
    private var permissionEventsSource: EventChannel.EventSink? = null
    private var permissionEventsSourceHandler: EventChannel.StreamHandler =
        object : EventChannel.StreamHandler {
            override fun onListen(arguments: Any?, events: EventChannel.EventSink?) {
                permissionEventsSource = events
            }

            override fun onCancel(arguments: Any?) {
                permissionEventsSource = null
            }
        }
    private lateinit var context: Context
    private lateinit var activity: Activity


    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, PLUGIN_CHANNEL_NAME)
        channel.setMethodCallHandler(this)
        context = flutterPluginBinding.applicationContext
        permissionEventsEmitter =
            EventChannel(flutterPluginBinding.binaryMessenger, PERMISSION_CHANNEL_NAME)
        permissionEventsEmitter?.setStreamHandler(permissionEventsSourceHandler)
    }

    override fun onMethodCall(@NonNull call: MethodCall, @NonNull result: Result) {
        when (call.method) {
            GET_PLATFORM_VERSION -> getPlatformVersion(result)
            CHECK_PERMISSIONS -> checkPermissions()
            REQUEST_PERMISSIONS -> requestPermissions()
            else -> result.notImplemented()
        }
    }

    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        activity = binding.activity
        binding.addActivityResultListener(this)
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
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
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

    companion object Constants {
        const val PLUGIN_CHANNEL_NAME = "another_location_plugin"
        const val PERMISSION_CHANNEL_NAME = "permission_event_channel"
        const val GET_PLATFORM_VERSION = "getPlatformVersion"
        const val CHECK_PERMISSIONS = "checkPermissions"
        const val REQUEST_PERMISSIONS = "requestPermissions"
        const val PERMISSION_ALLOWED = "Permission allowed"
        const val PERMISSION_NOT_ALLOWED = "Permission not allowed"
        const val REQUEST_CODE = 500
    }
}

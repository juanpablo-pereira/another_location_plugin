import 'dart:async';

import '../utils/strings.dart';
import 'package:flutter/services.dart';

class AnotherLocationPlugin {
  static const MethodChannel _channel =
      const MethodChannel(Strings.pluginChannelName);

  static const EventChannel _permissionEventChannel =
      EventChannel(Strings.permissionChannelName);
  static Stream<dynamic> permissionStream =
      _permissionEventChannel.receiveBroadcastStream();

  static const EventChannel _locationEventChannel =
      EventChannel(Strings.locationChannelName);
  static Stream<dynamic> locationStream =
      _locationEventChannel.receiveBroadcastStream();

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod(
      Strings.platformVersionMethod,
    );
    return version;
  }

  static Future<bool> get initializePlugin async {
    final bool isInitialized = await _channel.invokeMethod(
      Strings.initializePluginMethod,
    );
    return isInitialized;
  }

  static Future<bool> get checkPermissions async {
    final bool permissionsChecked = await _channel.invokeMethod(
      Strings.checkPermissionsMethod,
    );
    return permissionsChecked;
  }

  static Future<void> get requestPermissions async {
    await _channel.invokeMethod(
      Strings.requestPermissionsMethod,
    );
    return;
  }

  static Future<void> get lastCoordinates async {
    await _channel.invokeMethod(
      Strings.getLastLocationMethod,
    );
    return;
  }

  static Future<bool> get stopPlugin async {
    final bool isStopped = await _channel.invokeMethod(
      Strings.stopPluginMethod,
    );
    return isStopped;
  }
}

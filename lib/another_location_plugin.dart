import 'dart:async';

import 'package:flutter/services.dart';

class AnotherLocationPlugin {
  static const MethodChannel _channel =
      const MethodChannel('another_location_plugin');

  static Future<String?> get platformVersion async {
    final String? version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}

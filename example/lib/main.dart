import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:another_location_plugin/another_location_plugin.dart';

import 'utils/strings.dart';
import 'widgets/location_page.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = Strings.initialPlatformVersion;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  Future<void> initPlatformState() async {
    String platformVersion;

    try {
      platformVersion = await AnotherLocationPlugin.platformVersion ??
          Strings.platformVersionObtained;
    } on PlatformException {
      platformVersion = Strings.platformVersionException;
    }

    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: Scaffold(
        body: LocationPage(),
      ),
    );
  }
}

import 'package:flutter/material.dart';

import 'package:another_location_plugin/another_location_plugin.dart';
import '../utils/strings.dart';
import '../utils/dimensions.dart';
import '../utils/styles.dart';
import 'button.dart';

class LocationPage extends StatefulWidget {
  const LocationPage({
    Key? key,
  }) : super(key: key);

  @override
  _LocationCardState createState() => _LocationCardState();
}

class _LocationCardState extends State<LocationPage> {
  late bool _hasPermissions;
  late bool _isInitialized;

  @override
  void initState() {
    super.initState();
    _hasPermissions = false;
    _isInitialized = false;
  }

  @override
  Widget build(BuildContext context) {
    return Container(
      decoration: BoxDecoration(
        image: DecorationImage(
          image: AssetImage(
            Strings.backgroundImageUrl,
          ),
          fit: BoxFit.cover,
        ),
      ),
      child: Center(
        child: SizedBox(
          width: Dimensions.locationCardContentWidth,
          height: Dimensions.locationCardContentHeight,
          child: Padding(
            padding: const EdgeInsets.all(
              Dimensions.locationCardContentPadding,
            ),
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Text(
                  Strings.locationCardTitle,
                  style: titleStyle,
                  textAlign: TextAlign.left,
                ),
                StreamBuilder(
                  stream: AnotherLocationPlugin.locationStream,
                  initialData: Strings.locationCardHint,
                  builder: (context, snapshot) {
                    return snapshot.hasData
                        ? Container(
                            alignment: Alignment.centerLeft,
                            width: Dimensions.locationCardHintContainerSize,
                            child: Text(
                              '${snapshot.data}',
                              style: subtitleStyle,
                            ),
                          )
                        : Text(
                            Strings.locationEmptyText,
                          );
                  },
                ),
                Row(
                  children: [
                    Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        Button(
                          onPressedFunction: () =>
                              AnotherLocationPlugin.lastCoordinates,
                          enabled: _isInitialized,
                          icon: Icons.location_on,
                          text: Strings.locationCardFindButtonText,
                          style: mainButtonStyle,
                        ),
                        Button(
                          onPressedFunction: () =>
                              AnotherLocationPlugin.stopPlugin,
                          enabled: _isInitialized,
                          icon: Icons.stop,
                          text: Strings.locationCardStopButtonText,
                          style: mainButtonStyle,
                        ),
                        const SizedBox(
                          height: Dimensions.locationCardButtonsMargin,
                        ),
                        Button(
                          enabled: _hasPermissions,
                          onPressedFunction: () {
                            setState(() {
                              _isInitialized = true;
                            });
                            AnotherLocationPlugin.initializePlugin;
                          },
                          text: Strings.locationCardInitializeButtonText,
                          style: secondaryButtonStyle,
                        ),
                        Button(
                          enabled: true,
                          onPressedFunction: () {
                            setState(() {
                              _hasPermissions = true;
                            });
                            AnotherLocationPlugin.requestPermissions;
                          },
                          text: Strings.locationCardPermissionsButtonText,
                          style: secondaryButtonStyle,
                        ),
                        Button(
                          enabled: true,
                          onPressedFunction: () =>
                              AnotherLocationPlugin.checkPermissions,
                          text: Strings.locationCardCheckButtonText,
                          style: secondaryButtonStyle,
                        ),
                        StreamBuilder(
                          stream: AnotherLocationPlugin.permissionStream,
                          builder: (context, snapshot) {
                            return snapshot.hasData
                                ? Container(
                                    height: Dimensions
                                        .locationCardCheckMessageHeight,
                                    width: Dimensions
                                        .locationCardCheckMessageWidth,
                                    color: Colors.grey.shade900.withOpacity(
                                      Styles.colorOpacity800,
                                    ),
                                    child: Text(
                                      '${snapshot.data}',
                                      style: TextStyle(
                                        color: Colors.grey.shade400,
                                        height: Dimensions
                                            .locationCardCheckMessageLineHeight,
                                      ),
                                      textAlign: TextAlign.center,
                                    ),
                                  )
                                : Text(
                                    Strings.locationEmptyText,
                                  );
                          },
                        )
                      ],
                    ),
                  ],
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

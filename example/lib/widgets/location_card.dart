import 'package:flutter/material.dart';

import 'package:another_location_plugin/another_location_plugin.dart';
import '../utils/strings.dart';
import '../utils/dimensions.dart';
import '../utils/styles.dart';

class LocationCard extends StatefulWidget {
  const LocationCard({
    Key? key,
  }) : super(key: key);

  @override
  _LocationCardState createState() => _LocationCardState();
}

class _LocationCardState extends State<LocationCard> {
  @override
  Widget build(BuildContext context) {
    return Card(
      elevation: Dimensions.locationCardElevation,
      shape: RoundedRectangleBorder(
        borderRadius: BorderRadius.all(
          Radius.circular(
            Dimensions.locationCardBorderRadius,
          ),
        ),
      ),
      child: SizedBox(
        width: Dimensions.locationCardContentWidth,
        height: Dimensions.locationCardContentHeight,
        child: Padding(
          padding: const EdgeInsets.all(
            Dimensions.locationCardContentPadding,
          ),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: [
              Text(
                Strings.locationCardTitle,
                style: TextStyle(
                  fontSize: Styles.locationCardTitleFontSize,
                  fontWeight: FontWeight.w500,
                ),
                textAlign: TextAlign.center,
              ),
              Text(
                Strings.locationCardHint,
                style: TextStyle(
                  fontSize: Styles.locationCardHintFontSize,
                ),
              ),
              Column(
                children: [
                  ElevatedButton(
                    onPressed: () {
                      AnotherLocationPlugin.requestPermissions;
                    },
                    child: Text(
                      Strings.locationCardFindButtonText,
                    ),
                  ),
                  TextButton(
                    onPressed: () {
                      AnotherLocationPlugin.checkPermissions;
                    },
                    child: Text(
                      Strings.locationCardCheckButtonText,
                    ),
                  ),
                  StreamBuilder(
                    stream: AnotherLocationPlugin.permissionStream,
                    builder: (context, snapshot) {
                      if (snapshot.hasData) {
                        return Text(
                          '${snapshot.data}',
                        );
                      }
                      return Text(
                        Strings.locationEmptyText,
                      );
                    },
                  )
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}

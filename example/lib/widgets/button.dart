import 'package:flutter/material.dart';

import '../utils/dimensions.dart';

class Button extends StatelessWidget {
  const Button({
    Key? key,
    required this.onPressedFunction,
    required this.enabled,
    required this.text,
    required this.style,
    this.icon,
  }) : super(key: key);
  final void Function() onPressedFunction;
  final bool enabled;
  final String text;
  final ButtonStyle style;
  final IconData? icon;

  @override
  Widget build(BuildContext context) {
    return icon == null
        ? ElevatedButton(
            style: style,
            onPressed: enabled ? onPressedFunction : null,
            child: Text(
              text,
            ),
          )
        : ElevatedButton(
            style: style,
            onPressed: enabled ? onPressedFunction : null,
            child: SizedBox(
              width: Dimensions.locationCardFindButtonWidth,
              child: Row(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Icon(
                    icon,
                  ),
                  Text(
                    text,
                  ),
                ],
              ),
            ),
          );
  }
}

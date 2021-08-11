import 'package:flutter/material.dart';

abstract class Styles {
  static const double locationCardTitleFontSize = 42.0;
  static const double locationCardTextShadowBlur = 7.0;
  static const double locationCardHintFontSize = 31.0;
  static const double colorOpacity500 = 0.5;
  static const double colorOpacity600 = 0.6;
  static const double colorOpacity700 = 0.7;
  static const double colorOpacity800 = 0.8;
  static const double colorOpacity900 = 0.9;
}

final titleStyle = TextStyle(
  fontSize: Styles.locationCardTitleFontSize,
  fontWeight: FontWeight.w500,
  color: Colors.grey.shade100,
  shadows: [
    Shadow(
      color: Colors.black,
      blurRadius: Styles.locationCardTextShadowBlur,
    ),
  ],
);

final subtitleStyle = TextStyle(
  fontSize: Styles.locationCardHintFontSize,
  fontWeight: FontWeight.w700,
  color: Colors.grey.shade100,
  shadows: [
    Shadow(
      color: Colors.black,
      blurRadius: Styles.locationCardTextShadowBlur,
    ),
  ],
);

final mainButtonStyle = ButtonStyle(
  backgroundColor: MaterialStateProperty.resolveWith(
    (states) {
      if (states.contains(
        MaterialState.disabled,
      ))
        return Colors.purple.shade400.withOpacity(
          Styles.colorOpacity800,
        );
      return Colors.purple.shade500;
    },
  ),
  foregroundColor: MaterialStateProperty.resolveWith(
    (states) {
      if (states.contains(
        MaterialState.disabled,
      ))
        return Colors.grey.shade500.withOpacity(
          Styles.colorOpacity900,
        );
      return Colors.grey.shade200;
    },
  ),
);

final secondaryButtonStyle = ButtonStyle(
  backgroundColor: MaterialStateProperty.resolveWith(
    (states) {
      if (states.contains(
        MaterialState.disabled,
      ))
        return Colors.grey.shade500.withOpacity(
          Styles.colorOpacity700,
        );
      return Colors.grey.shade400.withOpacity(
        Styles.colorOpacity800,
      );
    },
  ),
  foregroundColor: MaterialStateProperty.resolveWith(
    (states) {
      if (states.contains(
        MaterialState.disabled,
      ))
        return Colors.grey.shade800.withOpacity(
          Styles.colorOpacity900,
        );
      return Colors.purple.shade700;
    },
  ),
);

package com.example.another_location_plugin

import com.google.android.gms.location.LocationResult
import kotlin.math.pow
import kotlin.math.round

fun LocationResult.convertToString(): String {
    return StringBuilder().append(AnotherLocationPlugin.LONGITUDE_TAG)
        .append(Constants.WHITE_SPACE)
        .append(this.lastLocation?.longitude?.run {
            this.cutToDecimals(Constants.DECIMAL_AMOUNT)
        })
        .append(Constants.TRIPLE_WHITE_SPACE)
        .append(AnotherLocationPlugin.LATITUDE_TAG)
        .append(Constants.WHITE_SPACE)
        .append(this.lastLocation?.latitude?.run {
            this.cutToDecimals(Constants.DECIMAL_AMOUNT)
        }).toString()
}

/*decimals are the amount*/
fun Double.cutToDecimals(decimals : Int) : Double {
    val digitCount = Constants.BASE.toDouble().pow(decimals)
    return round(this * digitCount) / digitCount
}


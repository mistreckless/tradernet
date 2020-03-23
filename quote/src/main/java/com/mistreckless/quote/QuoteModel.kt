package com.mistreckless.quote

import androidx.annotation.ColorRes
import androidx.annotation.StringRes

data class QuoteModel(
    val ticker: String,
    val percentSessionChange: String,
    val percentSessionChangeType: PercentSessionChangeType,
    val percentChangeType: PercentChangeType,
    val latestTradeExchangeName: String,
    val paperName: String,
    val latestTradePrice: String,
    val latestTradePoints: String,
    val logoUrl: String
)

enum class PercentSessionChangeType(@ColorRes val textColor: Int, @StringRes val textScheme: Int) {
    POSITIVE(R.color.green, R.string.quote_percent_change_positive),
    NEGATIVE(R.color.red, R.string.quote_percent_change_negative),
    NEUTRAL(R.color.black, R.string.quote_percent_change_neutral)
}

enum class PercentChangeType(@ColorRes val splashColor: Int) {
    POSITIVE(R.color.green), NEGATIVE(R.color.red), NONE(R.color.transparent)
}
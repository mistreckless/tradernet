package com.mistreckless.service.dto

data class TickersResponse(
    val tickers: List<String>,
    val code: Int
)

data class TickersRequest(
    val cmd: String,
    val params: TickersRequestParams
)


data class TickersRequestParams(
    val type: String,
    val exchange: String,
    val gainers: Boolean,
    val limit: Int
)
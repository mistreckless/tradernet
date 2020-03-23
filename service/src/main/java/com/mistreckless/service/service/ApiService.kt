package com.mistreckless.service.service

import com.mistreckless.service.dto.QuoteDto
import com.mistreckless.service.dto.TickersResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("securities/export")
    suspend fun getQuotes(
        @Query("tickers", encoded = true) tickers: String,
        @Query("params", encoded = true) params: String
    ): List<QuoteDto>

    @GET("api")
    suspend fun getTickers(@Query("q", encoded = false) jsonLine: String): TickersResponse
}
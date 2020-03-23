package com.mistreckless.quote

import com.mistreckless.service.dto.QuoteDto
import com.mistreckless.service.dto.QuoteMessageResponse
import com.mistreckless.service.dto.TickersResponse


val quoteDtoDefault = QuoteDto(
    c = "TICKER",
    chg = "0",
    ltp = "0",
    ltr = "EXCHANGE_NAME",
    min_step = "0",
    name = "PAPER_NAME",
    pcp = "0"
)

val quoteDtoPositive = QuoteDto(
    c = "TICKER",
    chg = "0",
    ltp = "0",
    ltr = "EXCHANGE_NAME",
    min_step = "0",
    name = "PAPER_NAME",
    pcp = "5"
)

val quoteDtoNegative = QuoteDto(
    c = "TICKER",
    chg = "0",
    ltp = "0",
    ltr = "EXCHANGE_NAME",
    min_step = "0",
    name = "PAPER_NAME",
    pcp = "-5"
)

val mockTickersResponse = TickersResponse(arrayListOf(), -1)
val mockQuoteDtoResponseList = listOf<QuoteDto>(quoteDtoDefault)
val mockMessageResponsePositive = QuoteMessageResponse(listOf(quoteDtoPositive))
val mockMessageResponseNegative = QuoteMessageResponse(listOf(quoteDtoNegative))
val mockMessageResponseNeutral = QuoteMessageResponse(listOf(quoteDtoDefault))
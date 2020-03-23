package com.mistreckless.service.utils

import com.mistreckless.service.dto.TickersRequest
import com.mistreckless.service.dto.TickersRequestParams

val params = arrayOf("c", "pcp", "ltr", "name", "ltp", "chg", "min_step")

fun generateDefaultTickersRequest() = TickersRequest(
    cmd = "getTopSecurities",
    params = TickersRequestParams(
        type = "stocks",
        exchange = "russia",
        gainers = false,
        limit = 30
    )
)

val tickersToWatchChanges = arrayOf(
    "RSTI",
    "GAZP",
    "MRKZ",
    "RUAL",
    "HYDR",
    "MRKS",
    "SBER",
    "FEES",
    "TGKA",
    "VTBR",
    "ANH.US",
    "VICL.US",
    "BURG.US",
    "NBL.US",
    "YETI.US",
    "WSFS.US",
    "NIO.US",
    "DXC.US",
    "MIC.US",
    "HSBC.US",
    "EXPN.EU",
    "GSK.EU",
    "SHP.EU",
    "MAN.EU",
    "DB1.EU",
    "MUV2.EU",
    "TATE.EU",
    "KGF.EU",
    "MGGT.EU",
    "SGGD.EU"
)
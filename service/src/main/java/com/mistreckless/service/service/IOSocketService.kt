package com.mistreckless.service.service

import com.google.gson.Gson
import com.mistreckless.service.BuildConfig
import com.mistreckless.service.dto.QuoteMessageResponse
import com.mistreckless.service.utils.toJsonArray
import io.socket.client.IO
import io.socket.client.Socket

interface SocketService {
    fun startListenQuotes(
        tickets: Array<String>,
        newMessageAppear: (response: QuoteMessageResponse) -> Unit
    )

    fun stopListenQuotes()
}

class IOSocketService : SocketService {

    private val socket by lazy {
        val options = IO.Options()
        if (BuildConfig.DEBUG) {
            options.query = "SID=lh1kkieqfj2l3bbjhi4bunjhqv"
        }
        options.reconnection = true
        options.reconnectionDelay = 100
        options.reconnectionDelayMax = 1000
        IO.socket(BuildConfig.WS_SERVER_URL, options)
    }

    override fun startListenQuotes(
        tickets: Array<String>,
        newMessageAppear: (response: QuoteMessageResponse) -> Unit
    ) {
        socket.on(Socket.EVENT_CONNECT) {
            socket.emit("sup_updateSecurities2", tickets.toJsonArray())
        }.on("q") { message ->
            val response = Gson().fromJson<QuoteMessageResponse>(
                message.first().toString(),
                QuoteMessageResponse::class.java
            )
            newMessageAppear(response)
        }
        socket.connect()
    }

    override fun stopListenQuotes() {
        socket.off("q")
    }
}

package com.mistreckless.service.wrapper

import com.mistreckless.service.dto.QuoteMessageResponse
import com.mistreckless.service.service.SocketService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.runBlocking

interface FlowSocketServiceWrapper {
    suspend fun listenQuotes(tickets: Array<String>): Flow<QuoteMessageResponse>
}

@ExperimentalCoroutinesApi
class DefaultFlowSocketServiceWrapper(private val socketService: SocketService) :
    FlowSocketServiceWrapper {

    override suspend fun listenQuotes(tickets: Array<String>): Flow<QuoteMessageResponse> {
        val channel = Channel<QuoteMessageResponse>()
        return flow<QuoteMessageResponse> {
            socketService.startListenQuotes(tickets) {
                runBlocking {
                    channel.send(it)
                }
            }
            channel.consumeEach {
                this.emit(it)
            }
        }
            .onCompletion {
                socketService.stopListenQuotes()
                channel.cancel()
            }
    }

}
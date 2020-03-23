package com.mistreckless.quote

import com.google.gson.Gson
import com.mistreckless.service.BuildConfig
import com.mistreckless.service.dto.QuoteDto
import com.mistreckless.service.dto.QuoteMessageResponse
import com.mistreckless.service.service.ApiService
import com.mistreckless.service.utils.generateDefaultTickersRequest
import com.mistreckless.service.utils.params
import com.mistreckless.service.utils.toQueryLine
import com.mistreckless.service.wrapper.FlowSocketServiceWrapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.coroutines.coroutineContext

interface QuoteInteractor {
    suspend fun listenQuotes(): Flow<List<QuoteModel>>
}

@ExperimentalCoroutinesApi
class DefaultQuoteInteractor(
    private val flowSocketService: FlowSocketServiceWrapper,
    private val apiService: ApiService
) : QuoteInteractor {

    private val stateChannel by lazy {
        BroadcastChannel<State>(1)
    }

    override suspend fun listenQuotes(): Flow<List<QuoteModel>> {
        coroutineContext[Job]?.invokeOnCompletion {
            stateChannel.cancel()
        }
        fetchFlow()
        return fetchFlow()
            .flatMapLatest { models ->
                merge(flow { emit(models) }, subscribeFlow(models))
            }
            .flowOn(Dispatchers.IO)

    }

    private suspend fun fetchFlow() = flow {
        val queryLine = Gson().toJson(generateDefaultTickersRequest())
        val tickersResponse = apiService.getTickers(queryLine)
        val quotesResponse = apiService
            .getQuotes(tickersResponse.tickers.toTypedArray().toQueryLine(), params.toQueryLine())
        val models = quotesResponse
            .toInitialModels()
        emit(models)
    }


    private suspend fun subscribeFlow(models: List<QuoteModel>): Flow<List<QuoteModel>> {
        val subscription = stateChannel.openSubscription()
        stateChannel.send(State(models))
        return subscription.consumeAsFlow()
            .zip(
                flowSocketService.listenQuotes(
                    models.map(QuoteModel::ticker).toTypedArray()
                )
            ) { state, response ->
                mapResponseToState(response, state)
            }
            .onEach(stateChannel::send)
            .map { state -> state.models }
            .onCompletion {
                subscription.cancel()
            }
    }


    private fun mapResponseToState(response: QuoteMessageResponse, state: State): State =
        State(
            state.models.map { model ->
                response.q.find { it.c == model.ticker }?.applyChangesToModel(model) ?: model
            }
        )

    private fun QuoteDto.applyChangesToModel(model: QuoteModel): QuoteModel {
        val percentChange = pcp ?: model.percentSessionChange
        val percentSessionChangeType =
            pcp?.toDoubleOrNull()?.definePercentSessionChangeType()
                ?: model.percentSessionChangeType
        val percentChangeType = pcp?.toDoubleOrNull()
            ?.definePercentChangeType(model.percentSessionChange.toDoubleOrNull())
            ?: model.percentChangeType

        val paperName = name ?: model.paperName
        val latestTradePrice = ltp ?: model.latestTradePrice
        val latestTradePoints = ltp ?: model.latestTradePoints
        val latestTradeExchangeName = ltr ?: model.latestTradeExchangeName
        return model.copy(
            percentSessionChange = percentChange,
            percentSessionChangeType = percentSessionChangeType,
            latestTradePrice = latestTradePrice,
            latestTradePoints = latestTradePoints,
            latestTradeExchangeName = latestTradeExchangeName,
            paperName = paperName,
            percentChangeType = percentChangeType
        )
    }

    private fun Double.definePercentChangeType(oldValue: Double?): PercentChangeType =
        when {
            oldValue == null || oldValue == this -> PercentChangeType.NONE
            oldValue > this -> PercentChangeType.NEGATIVE
            else -> PercentChangeType.POSITIVE
        }

    private fun Double.definePercentSessionChangeType(): PercentSessionChangeType =
        when {
            this > 0 -> PercentSessionChangeType.POSITIVE
            this < 0 -> PercentSessionChangeType.NEGATIVE
            else -> PercentSessionChangeType.NEUTRAL
        }

    private fun List<QuoteDto>.toInitialModels(): List<QuoteModel> {

        return this.map {
            QuoteModel(
                ticker = it.c,
                paperName = it.name ?: "",
                latestTradeExchangeName = it.ltr ?: "",
                latestTradePoints = it.chg ?: "",
                latestTradePrice = it.ltp ?: "",
                percentSessionChange = it.pcp ?: "",
                percentSessionChangeType = it.pcp?.toDoubleOrNull()
                    ?.definePercentSessionChangeType() ?: PercentSessionChangeType.NEUTRAL,
                percentChangeType = PercentChangeType.NONE,
                logoUrl = "${BuildConfig.SERVER_URL}logos/get-logo-by-ticker?ticker=${it.c.toLowerCase(
                    Locale.ENGLISH
                )}"
            )
        }
    }
}

private data class State(val models: List<QuoteModel>)

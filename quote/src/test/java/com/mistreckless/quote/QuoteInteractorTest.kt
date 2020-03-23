package com.mistreckless.quote

import com.mistreckless.service.service.ApiService
import com.mistreckless.service.wrapper.FlowSocketServiceWrapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.anyString
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class QuoteInteractorTest {

    lateinit var apiService: ApiService

    @Before
    fun before() {
        apiService = mock<ApiService> {
            on {
                runBlocking {
                    getTickers(anyString())
                }
            } doReturn mockTickersResponse

            on {
                runBlocking {
                    getQuotes(anyString(), anyString())
                }
            } doReturn mockQuoteDtoResponseList
        }
    }

    @Test
    fun `test listenQuotes flow values, when percent change is positive`() = runBlocking {
        val wrapperFlow = mock<FlowSocketServiceWrapper> {
            on {
                runBlocking {
                    listenQuotes(any())
                }
            } doReturn flow {
                emit(mockMessageResponsePositive)
            }
        }

        val interactor = DefaultQuoteInteractor(wrapperFlow, apiService)

        val result = interactor.listenQuotes().toList()

        assertEquals(result.size, 2)

        val firstModelList = result.first()
        val secondModelList = result.last()

        assertEquals(firstModelList.first().percentChangeType, PercentChangeType.NONE)
        assertEquals(
            firstModelList.first().percentSessionChangeType,
            PercentSessionChangeType.NEUTRAL
        )

        assertEquals(secondModelList.first().percentChangeType, PercentChangeType.POSITIVE)
        assertEquals(
            secondModelList.first().percentSessionChangeType,
            PercentSessionChangeType.POSITIVE
        )
    }

    @Test
    fun `test listenQuotes flow values, when percent change is negative`() = runBlocking {
        val wrapperFlow = mock<FlowSocketServiceWrapper> {
            on {
                runBlocking {
                    listenQuotes(any())
                }
            } doReturn flow {
                emit(mockMessageResponseNegative)
            }
        }

        val interactor = DefaultQuoteInteractor(wrapperFlow, apiService)

        val result = interactor.listenQuotes().toList()

        assertEquals(result.size, 2)

        val firstModelList = result.first()
        val secondModelList = result.last()

        assertEquals(firstModelList.first().percentChangeType, PercentChangeType.NONE)
        assertEquals(
            firstModelList.first().percentSessionChangeType,
            PercentSessionChangeType.NEUTRAL
        )

        assertEquals(secondModelList.first().percentChangeType, PercentChangeType.NEGATIVE)
        assertEquals(
            secondModelList.first().percentSessionChangeType,
            PercentSessionChangeType.NEGATIVE
        )
    }

    @Test
    fun `test listenQuotes flow values, when percent change is neutral`() = runBlocking {
        val wrapperFlow = mock<FlowSocketServiceWrapper> {
            on {
                runBlocking {
                    listenQuotes(any())
                }
            } doReturn flow {
                emit(mockMessageResponseNeutral)
            }
        }

        val interactor = DefaultQuoteInteractor(wrapperFlow, apiService)

        val result = interactor.listenQuotes().toList()

        assertEquals(result.size, 2)

        val firstModelList = result.first()
        val secondModelList = result.last()

        assertEquals(firstModelList.first().percentChangeType, PercentChangeType.NONE)
        assertEquals(
            firstModelList.first().percentSessionChangeType,
            PercentSessionChangeType.NEUTRAL
        )

        assertEquals(secondModelList.first().percentChangeType, PercentChangeType.NONE)
        assertEquals(
            secondModelList.first().percentSessionChangeType,
            PercentSessionChangeType.NEUTRAL
        )
    }

}


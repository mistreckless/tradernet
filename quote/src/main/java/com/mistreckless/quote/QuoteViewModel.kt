package com.mistreckless.quote

import com.mistreckless.ui_core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class QuoteState {
    object Loading : QuoteState()
    object Error : QuoteState()
    data class Updated(val models: List<QuoteModel>) : QuoteState()
}

@ExperimentalCoroutinesApi
class QuoteViewModel(private val interactor: QuoteInteractor) : BaseViewModel<QuoteState>() {

    fun listenQuotes() {
        launch {
            listen()
        }
    }

    fun retry() {
        launch {
            listen()
        }
    }

    private suspend fun listen() = interactor.listenQuotes()
        .onStart { liveData.postValue(QuoteState.Loading) }
        .onEach { liveData.postValue(QuoteState.Updated(it)) }
        .catch { liveData.postValue(QuoteState.Error) }
        .flowOn(Dispatchers.Main)
        .collect()

}
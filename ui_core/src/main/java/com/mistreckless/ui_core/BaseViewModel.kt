package com.mistreckless.ui_core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
abstract class BaseViewModel<STATE : Any> : ViewModel(), CoroutineScope {
    private val job = Job()

    override val coroutineContext: CoroutineContext = Dispatchers.Default + job

    protected val liveData by lazy {
        MutableLiveData<STATE>()
    }

    fun observeState(): LiveData<STATE> = liveData

    override fun onCleared() {
        job.cancel()
        super.onCleared()
    }
}
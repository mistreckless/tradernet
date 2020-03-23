package com.mistreckless.quote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mistreckless.service.service.ApiService
import com.mistreckless.service.wrapper.FlowSocketServiceWrapper
import com.mistreckless.ui_core.PerFragment
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
@Module
object QuoteModule {

    @PerFragment
    @Provides
    @JvmStatic
    fun provideQuoteViewModel(fragment: Quote, factory: QuoteViewModelFactory): QuoteViewModel =
        ViewModelProvider(fragment, factory).get(QuoteViewModel::class.java)

    @PerFragment
    @Provides
    @JvmStatic
    fun provideQuoteInteractor(
        flowSocketServiceWrapper: FlowSocketServiceWrapper,
        apiService: ApiService
    ): QuoteInteractor =
        DefaultQuoteInteractor(flowSocketServiceWrapper, apiService)
}

@ExperimentalCoroutinesApi
@Suppress("UNCHECKED_CAST")
class QuoteViewModelFactory @Inject constructor(private val interactor: QuoteInteractor) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(QuoteViewModel::class.java))
        return QuoteViewModel(interactor) as T
    }
}
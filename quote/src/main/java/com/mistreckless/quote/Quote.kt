package com.mistreckless.quote

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mistreckless.ui_core.BaseFragment
import com.mistreckless.ui_core.GlideApp
import kotlinx.android.synthetic.main.fragment_quote.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class Quote : BaseFragment<QuoteViewModel, QuoteState>() {
    override val layoutId: Int = R.layout.fragment_quote

    @Inject
    override lateinit var viewModel: QuoteViewModel

    private val adapter by lazy {
        QuoteAdapter(GlideApp.with(this))
    }

    override fun init(firstLaunch: Boolean) {
        quoteRecyclerView.adapter = adapter
        quoteRecyclerView.layoutManager = LinearLayoutManager(context)
        quoteRecyclerView.setHasFixedSize(true)
        quoteBtnRetry.setOnClickListener {
            viewModel.retry()
        }
        if (firstLaunch) {
            viewModel.listenQuotes()
        }
    }

    override fun renderState(state: QuoteState) {
        when (state) {
            QuoteState.Loading -> {
                quoteRecyclerView.visibility = View.GONE
                quoteErrorView.visibility = View.GONE
                quoteProgressBar.visibility = View.VISIBLE
            }
            QuoteState.Error -> {
                quoteRecyclerView.visibility = View.GONE
                quoteProgressBar.visibility = View.GONE
                quoteErrorView.visibility = View.VISIBLE
            }
            is QuoteState.Updated -> {
                quoteRecyclerView.visibility = View.VISIBLE
                quoteProgressBar.visibility = View.GONE
                quoteErrorView.visibility = View.GONE
                adapter.updateItems(state.models)
            }
        }
    }

}
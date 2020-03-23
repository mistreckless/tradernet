package com.mistreckless.ui_core

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import dagger.android.support.AndroidSupportInjection.inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

@ExperimentalCoroutinesApi
abstract class BaseFragment<VM : BaseViewModel<STATE>, STATE : Any> :
    Fragment(), CoroutineScope {
    abstract val layoutId: Int

    lateinit var job: Job

    abstract var viewModel: VM

    private var firstLaunch: Boolean = true

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    abstract fun renderState(state: STATE)

    abstract fun init(firstLaunch: Boolean)

    override fun onAttach(context: Context) {
        inject(this)
        retainInstance = true
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        job = Job()
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.observeState().observe(this, Observer(this::renderState))
        init(firstLaunch)
        firstLaunch = false
    }


    override fun onDestroyView() {
        job.cancel()
        super.onDestroyView()
    }
}
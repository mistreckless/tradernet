package com.mistreckless.tradernet

import android.app.Application
import com.mistreckless.service.ServiceModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class App : Application(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun onCreate() {
        super.onCreate()
        DaggerAppComponent.builder()
            .application(this)
            .serviceModule(ServiceModule)
            .build()
            .inject(this)
    }

    override fun androidInjector(): AndroidInjector<Any> = dispatchingAndroidInjector
}
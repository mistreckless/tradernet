package com.mistreckless.tradernet

import com.mistreckless.quote.Quote
import com.mistreckless.quote.QuoteModule
import com.mistreckless.service.ServiceModule
import com.mistreckless.ui_core.PerActivity
import com.mistreckless.ui_core.PerFragment
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import dagger.android.AndroidInjectionModule
import dagger.android.ContributesAndroidInjector
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Singleton
@Component(
    modules = [(AndroidInjectionModule::class),
        (ServiceModule::class),
        (ActivityBuilder::class)]
)
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: App): Builder

        fun serviceModule(serviceModule: ServiceModule): Builder
        fun build(): AppComponent
    }

    fun inject(app: App)
}

@ExperimentalCoroutinesApi
@Module
abstract class ActivityBuilder {

    @PerActivity
    @ContributesAndroidInjector(modules = [FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity
}

@Module
abstract class FragmentBuilder {

    @PerFragment
    @ContributesAndroidInjector(modules = [QuoteModule::class])
    abstract fun bindQuoteFragment(): Quote
}
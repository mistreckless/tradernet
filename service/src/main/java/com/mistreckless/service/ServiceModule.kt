package com.mistreckless.service

import com.mistreckless.service.service.ApiService
import com.mistreckless.service.service.IOSocketService
import com.mistreckless.service.service.SocketService
import com.mistreckless.service.wrapper.DefaultFlowSocketServiceWrapper
import com.mistreckless.service.wrapper.FlowSocketServiceWrapper
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@Module
object ServiceModule {

    @Provides
    @Singleton
    @JvmStatic
    fun provideApiService(): ApiService {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(logging)
        val client = builder.build()

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.SERVER_URL)
            .client(client)
            .build()
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    @Singleton
    @JvmStatic
    fun provideFlowSocketServiceWrapper(): FlowSocketServiceWrapper =
        DefaultFlowSocketServiceWrapper(IOSocketService())


    @Provides
    @Singleton
    @JvmStatic
    fun provideSocketService(): SocketService = IOSocketService()
}
package code_setup.app_util.di

import android.app.Application
import android.content.res.Resources
import com.base.di.module.AppModule
import com.base.di.module.OkHttpModule
import com.base.di.module.RetrofitModule
import com.base.util.AppSchedulerProvider
import com.electrovese.kotlindemo.networking.ApiInterface
import com.google.gson.Gson
import dagger.Component
import io.reactivex.disposables.CompositeDisposable
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by arischoice on 20/1/2019.
 */
@Singleton
@Component(modules = arrayOf(AppModule::class, RetrofitModule::class, ApiModule::class, OkHttpModule::class))
interface AppComponent {
    fun application(): Application
    fun gson(): Gson
    fun resources(): Resources
    fun retrofit(): Retrofit
    fun endpoints(): ApiInterface
    fun cache(): Cache
    fun client(): OkHttpClient
    fun loggingInterceptor(): HttpLoggingInterceptor
    fun compositeDisposable(): CompositeDisposable
    fun schedulerProvider(): AppSchedulerProvider

}
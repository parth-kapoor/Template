package com.base.di.module

import android.app.Application
import com.base.util.AppSchedulerProvider
import com.burakeregar.githubsearch.home.presenter.OnboardPresenter
import code_setup.app_util.di.ActivityScope
import code_setup.ui_.home.home_mvp.HomePresenter
import com.electrovese.kotlindemo.networking.ApiInterface
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Singleton


/**
 * Created by arischoice on 20/1/2019.
 */
@Module
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun providesGson() = GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create()

    @Provides
    @Singleton
    fun providesApplication() = application

    @Provides
    @Singleton
    fun providesResources() = application.resources

    /* @Provides
     @Singleton
     fun providesSharedPref() = Prefs.preferences*/

    @Provides
    @Singleton
    fun provideCompositeDisposable() = CompositeDisposable()

    @Provides
    @Singleton
    fun provideSchedulerProvider() = AppSchedulerProvider()


    @Provides
    @ActivityScope
    internal fun providesHomePresenter(
        api: ApiInterface,
        disposable: CompositeDisposable,
        scheduler: AppSchedulerProvider
    ): HomePresenter =
        HomePresenter(api, disposable, scheduler)

    @Provides
    @ActivityScope
    internal fun providesOnboardPresenter(
        api: ApiInterface,
        disposable: CompositeDisposable,
        scheduler: AppSchedulerProvider
    ): OnboardPresenter =
        OnboardPresenter(api, disposable, scheduler)
}
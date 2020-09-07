package code_setup.ui_.home.di_home

import com.base.util.AppSchedulerProvider
import code_setup.app_util.di.ActivityScope
import code_setup.ui_.home.home_mvp.HomePresenter
import com.electrovese.kotlindemo.networking.ApiInterface
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class HomeModule {

    @Provides
    @ActivityScope
    internal fun providesHomePresenter(api: ApiInterface, disposable: CompositeDisposable, scheduler: AppSchedulerProvider): HomePresenter =
            HomePresenter(api, disposable, scheduler)
}

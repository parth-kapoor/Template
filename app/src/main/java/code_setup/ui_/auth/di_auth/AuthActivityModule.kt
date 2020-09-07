package code_setup.ui_.auth.di_auth

import com.base.util.AppSchedulerProvider
import code_setup.ui_.auth.auth_mvp.AuthPresenter
import code_setup.app_util.di.ActivityScope
import com.electrovese.kotlindemo.networking.ApiInterface
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class AuthActivityModule {

    @Provides
    @ActivityScope
    internal fun providesAuthPresenter(
        api: ApiInterface,
        disposable: CompositeDisposable,
        scheduler: AppSchedulerProvider
    ): AuthPresenter =
        AuthPresenter(api, disposable, scheduler)
}

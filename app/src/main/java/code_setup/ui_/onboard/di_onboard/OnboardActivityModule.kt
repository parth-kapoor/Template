package code_setup.ui_.onboard.di_onboard

import com.base.util.AppSchedulerProvider
import code_setup.app_util.di.ActivityScope
import com.burakeregar.githubsearch.home.presenter.OnboardPresenter
import com.electrovese.kotlindemo.networking.ApiInterface
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class OnboardActivityModule {

    @Provides
    @ActivityScope
    internal fun providesOnboardPresenter(
        api: ApiInterface,
        disposable: CompositeDisposable,
        scheduler: AppSchedulerProvider
    ): OnboardPresenter =
        OnboardPresenter(api, disposable, scheduler)
}

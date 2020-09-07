package code_setup.ui_.settings.di_settings

import com.base.util.AppSchedulerProvider
import code_setup.app_util.di.ActivityScope
import code_setup.ui_.home.home_mvp.HomePresenter
import code_setup.ui_.settings.settings_mvp.SettingsPresenter
import com.electrovese.kotlindemo.networking.ApiInterface
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
class SettingsModule {

    @Provides
    @ActivityScope
    internal fun providesSettingsPresenter(
        api: ApiInterface,
        disposable: CompositeDisposable,
        scheduler: AppSchedulerProvider
    ): SettingsPresenter =
        SettingsPresenter(api, disposable, scheduler)
}

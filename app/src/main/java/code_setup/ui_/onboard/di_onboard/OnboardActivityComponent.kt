package code_setup.ui_.auth.di_auth


import code_setup.app_util.di.ActivityScope
import code_setup.app_util.di.AppComponent
import code_setup.ui_.onboard.di_onboard.OnboardActivityModule
import dagger.Component

@ActivityScope
@Component(
    dependencies = arrayOf(AppComponent::class),
    modules = arrayOf(OnboardActivityModule::class)
)
interface OnboardActivityComponent {


}

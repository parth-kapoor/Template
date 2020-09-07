package code_setup.ui_.auth.di_auth


import code_setup.app_util.di.ActivityScope
import code_setup.app_util.di.AppComponent
import code_setup.ui_.auth.views.authantication_.PasswordActivity
import code_setup.ui_.auth.views.authantication_.SignupActivity
import dagger.Component

@ActivityScope
@Component(
    dependencies = arrayOf(AppComponent::class),
    modules = arrayOf(AuthActivityModule::class)
)
interface AuthActivityComponent {


    abstract fun inject(passwordActivity: PasswordActivity)
    abstract fun inject(passwordActivity: SignupActivity)
}

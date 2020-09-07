package code_setup.ui_.settings.di_settings

import code_setup.ui_.home.views.HomeActivity
import code_setup.app_util.di.ActivityScope
import code_setup.app_util.di.AppComponent
import code_setup.ui_.home.views.rate.RateTripActivity
import code_setup.ui_.settings.ProfileActivity
import code_setup.ui_.settings.views.ridehistory.RideHistoryActivity
import code_setup.ui_.settings.views.support.SupportActivity

import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(SettingsModule::class))
interface SettingsComponent {
    fun inject(profileActivity: ProfileActivity)
    fun inject(profileActivity: RideHistoryActivity)
    fun inject(rateTripActivity: RateTripActivity)
    abstract fun inject(supportActivity: SupportActivity)


}

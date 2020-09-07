package code_setup.ui_.home.di_home

import code_setup.ui_.home.views.HomeActivity
import code_setup.app_util.di.ActivityScope
import code_setup.app_util.di.AppComponent
import code_setup.ui_.home.views.fragments.DeliveryUpdatesFragment
import code_setup.ui_.home.views.UpcomingBottomSheet
import code_setup.ui_.home.views.UpcomingToursFragment
import code_setup.ui_.home.views.fragments.BookingFragment
import code_setup.ui_.home.views.fragments.TripUpdatesFragment


import dagger.Component

@ActivityScope
@Component(dependencies = arrayOf(AppComponent::class), modules = arrayOf(HomeModule::class))
interface HomeComponent {
    fun inject(homeActivity: HomeActivity)

    fun inject(upcomingToursFragment: UpcomingToursFragment)
    fun inject(upcomingBottomSheet: UpcomingBottomSheet)
    fun inject(tourUpdatesFragment: DeliveryUpdatesFragment)
    abstract fun inject(bookingFragment: BookingFragment)
    abstract fun inject(tripUpdatesFragment: TripUpdatesFragment)

}

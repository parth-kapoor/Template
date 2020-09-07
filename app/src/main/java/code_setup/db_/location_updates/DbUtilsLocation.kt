package code_setup.db_.locations_record

import android.content.Context
import android.location.Location
import code_setup.app_util.DateUtilizer
import code_setup.app_util.location_utils.log
import code_setup.db_.AppDatabase
import com.google.gson.Gson

/***
 ***  Utility class to operate database recent Location @RecentLocation table
 ***/
class DbUtilsLocation {
    companion object {
        var TAG: String = DbUtilsLocation::class.java.simpleName
        fun saveCurrentLocation(
            context: Context,
            recentLocation: Location?,
            isMock: Boolean
        ) {
            var cureentLocation = RecentLocations()

            cureentLocation.saveDateTime =
                DateUtilizer.getCurrentDate(DateUtilizer.DEFAULT_DATE_TIME_FORMAT)
            cureentLocation.timeMillisec = System.currentTimeMillis()
            cureentLocation.ulocationLat = recentLocation!!.latitude
            cureentLocation.ulocationLng = recentLocation!!.longitude
            cureentLocation.isMockLocation = isMock
            AppDatabase.getAppDatabase(context).recentLocationDao().deleteAll()
            AppDatabase.getAppDatabase(context).recentLocationDao().insertAll(cureentLocation)

            log(TAG + "locationSaved " + Gson().toJson(cureentLocation))
            log(TAG + "locationSaved " + Gson().toJson(AppDatabase.getAppDatabase(context).recentLocationDao().all))
        }

        fun getCurrentLocation(
            context: Context
        ): RecentLocations {
            var locationData = AppDatabase.getAppDatabase(context).recentLocationDao().all

            log(TAG + "getCurrentLocation " + Gson().toJson(locationData))
            if (locationData != null && locationData.size > 0) {
                return locationData.get(0)
            } else
                return RecentLocations()

        }
    }
}
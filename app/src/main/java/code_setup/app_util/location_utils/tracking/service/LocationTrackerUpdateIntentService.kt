package code_setup.app_util.location_utils.tracking.service

import android.content.Context
import android.content.Intent
import androidx.core.app.JobIntentService
import com.google.android.gms.location.LocationResult


public open class LocationTrackerUpdateIntentService : JobIntentService() {

    override fun onHandleWork(intent: Intent) {
        onLocationResult(LocationResult.extractResult(intent) ?: return)
    }

    companion object {
        val JOB_ID = 0x01
        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, LocationTrackerUpdateIntentService::class.java, JOB_ID, work)
        }
    }

    open fun onLocationResult(locationResult: LocationResult) {}
}
package code_setup.app_util.location_utils.tracking.service

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.JobIntentService
import code_setup.app_core.BaseApplication
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_models.request_.UpdateOrderRequestModel
import code_setup.app_models.response_.BaseResponseModel
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.location_utils.MockLocationUtils
import com.google.android.gms.location.LocationResult
import code_setup.app_util.location_utils.geo_locator.tracking.LocationTracker
import code_setup.app_util.location_utils.getSharedPrefs
import code_setup.app_util.location_utils.log
import code_setup.db_.locations_record.DbUtilsLocation
import code_setup.net_.NetworkConstant
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.layout_side_drawer.*
import org.greenrobot.eventbus.EventBus
import kotlin.collections.ArrayList
import kotlin.collections.HashSet


/**
 * Receiver for handling location updates.
 *
 * For apps targeting API level O
 * [android.app.PendingIntent.getBroadcast] should be used when
 * requesting location updates. Due to limits on background services,
 * [android.app.PendingIntent.getService] should not be used.
 *
 * Note: Apps running on "O" devices (regardless of targetSdkVersion) may receive updates
 * less frequently than the interval specified in the
 * [com.google.android.gms.location.LocationRequest] when the app is no longer in the
 * foreground.
 */
class LocationTrackerUpdateBroadcastReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onReceive(context: Context, intent: Intent?) {

        log("onReceive $intent")

        if (intent == null) return

        val action = intent.action

        if (ACTION_PROCESS_UPDATES != action) return

        val result = try {
            LocationResult.extractResult(intent) ?: return

        } catch (e: Exception) {
            log(e.message)
            return
        }

        try {
            var isMock: Boolean
            var recentLocation = result.getLastLocation()
            log("recentLocation = $recentLocation")
            log("IS MOCK LOCATION  = " + " ->   " + MockLocationUtils.isMockLocation(recentLocation))
            if (MockLocationUtils.isMockLocation(recentLocation)) {
                isMock = false
                AppUtils.showToast(" You are using mock location ")
            } else {
                isMock = false
            }

            Prefs.putDouble(CommonValues.LATITUDE, recentLocation.latitude)
            Prefs.putDouble(CommonValues.LONGITUDE, recentLocation.longitude)
            DbUtilsLocation.saveCurrentLocation(context, recentLocation, isMock)
            EventBus.getDefault().postSticky(CustomEvent<Any>(EVENTS.CURRENT_LOCATION, true))
        } catch (e: Exception) {
            log("recentLocation " + "   ERROR ")
        }

//        log("result = $result")
//        getListOfFakeLocationApps(context)

        val intentClassName = context.getSharedPrefs().getString(LocationTracker.PREFS_NAME, "")
//        log("$intentClassName")

        JobIntentService.enqueueWork(
            context,
            Class.forName(intentClassName!!),
            12445,
            intent
        )

    }

    var mCompositeDisposable: CompositeDisposable? = null
    fun updatelocation(recentLocation: Location) {
        mCompositeDisposable = CompositeDisposable()
        val apiService = RestConfig.create()
        mCompositeDisposable?.add(
            apiService.updateLocationRequest(
                BaseApplication.instance.getCommonHeaders(),
                UpdateOrderRequestModel(
                    recentLocation.latitude, recentLocation.longitude,
                    Prefs.getString(CommonValues.CURRENT_ORDER_ID, "noid")!!
                )
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSuccess, this::handleFaliur)
        )
    }

    fun handleSuccess(baseResponse: BaseResponseModel) {
        var bResponse = baseResponse
        if (bResponse.response_code == NetworkConstant.SUCCESS) {
            log("handleSuccess if " + "   LOCATION UPDATE ")
        } else {
            log("handleSuccess else " + "   LOCATION UPDATE ")
        }
    }

    fun handleFaliur(error: Throwable) {
        log("handleFaliur " + "   LOCATION UPDATE ")
    }

    @SuppressLint("NewApi")
    fun getRunningApps(
        context: Context,
        includeSystem: Boolean
    ): List<String?> {
        val activityManager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningApps: HashSet<String> = HashSet()
        try {
            val runAppsList =
                activityManager.runningAppProcesses
            for (processInfo in runAppsList) {
                runningApps.addAll(mutableListOf(processInfo.pkgList.toString()))
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        try { //can throw securityException at api<18 (maybe need "android.permission.GET_TASKS")
            val runningTasks =
                activityManager.getRunningTasks(1000)
            for (taskInfo in runningTasks) {
                runningApps.add(taskInfo.topActivity!!.packageName)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        try {
            val runningServices =
                activityManager.getRunningServices(1000)
            for (serviceInfo in runningServices) {
                runningApps.add(serviceInfo.service.packageName)
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
        return ArrayList(runningApps)
    }

    fun isSystemPackage(
        context: Context,
        app: String?
    ): Boolean {
        val packageManager = context.packageManager
        try {
            Log.d(" isSystemPackage ", " " + app)
            val pkgInfo = packageManager.getPackageInfo(app, 0)
            return pkgInfo.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM != 0
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    fun hasAppPermission(
        context: Context,
        app: String?,
        permission: String
    ): Boolean {
        val packageManager = context.packageManager
        val packageInfo: PackageInfo
        try {
            packageInfo = packageManager.getPackageInfo(app, PackageManager.GET_PERMISSIONS)
            if (packageInfo.requestedPermissions != null) {
                for (requestedPermission in packageInfo.requestedPermissions) {
                    if (requestedPermission == permission) {
                        return true
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return false
    }

    fun getApplicationName(
        context: Context,
        packageName: String?
    ): String? {
        var appName = packageName
        val packageManager = context.packageManager
        try {
            appName = packageManager.getApplicationLabel(
                packageManager.getApplicationInfo(
                    packageName,
                    PackageManager.GET_META_DATA
                )
            ).toString()
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return appName
    }


    companion object {

        internal val ACTION_PROCESS_UPDATES =
            "com.electrovese.grocerydriver.geolocator.tracking.service" + ".PROCESS_UPDATES"
    }
}
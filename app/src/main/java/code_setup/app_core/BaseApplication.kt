package code_setup.app_core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.provider.Settings
import android.util.Log
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import com.base.di.module.AppModule
import code_setup.app_util.Prefs
import code_setup.app_util.di.AppComponent
import code_setup.app_util.di.DaggerAppComponent
import code_setup.ui_.home.models.other.SelectedLocationModel
import com.facebook.drawee.backends.pipeline.Fresco
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import kotlin.properties.Delegates

/**
 * Created by Harish on 20/1/2019.
 */
class BaseApplication : Application() {
    companion object {
        @JvmStatic
        lateinit var appComponent: AppComponent

        lateinit var instance: BaseApplication
        var recentNetworkStatus = true
        var scheduledDate: String = ""
        var bookingType: String = CommonValues.NORMAL_BOOKING_TYPE
        var selectedMyLocation: SelectedLocationModel? = null
        var selectedDestinationLocation: SelectedLocationModel? = null

    }

    var CHAT_OPENED: Boolean = false
    var CURRENT_VIEW_HOME: Int = 0
    override fun onCreate() {
        super.onCreate()
        instance = this
        initDagger()
        initPref()
        Fresco.initialize(this)
    }

    private fun initDagger() {
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    private fun initPref() {
        // sharedPreference initialization
        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()
    }

    // Get required API headers
    fun getCommonHeaders(): HashMap<String, String> {
        var map: HashMap<String, String> = HashMap()
        if (true) {
            map.put("Authorization", Prefs.getString(CommonValues.ACCESS_TOKEN, "")!!)
            map.put("timezone", AppUtils.getTimeZoneWithOffset())
//                map.put("Content-Type", "multipart/form-data");
        }
        return map
    }

    /**
     * Generate fcm token from @activity context for push notification
     */
    fun generateFirebaseToken(
        ctx: Activity
    ) {
        try {
            FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener(
                ctx
            ) { instanceIdResult ->
                val newToken = instanceIdResult.token
                try {
                    saveDviceId()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.d("TAG", " ANDROID ID " + "  ERROR ")
                }

                Prefs.putString(CommonValues.FCM_TOKEN, newToken)
                Log.d("newToken", newToken)
            }

        } catch (error: Exception) {
            error.printStackTrace()
        }
    }

    private fun saveDviceId() {
        val androidId = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)
        Prefs.putString(CommonValues.DEVICE_ID, androidId)
    }

    /**
     * Activity  get current USer Data
     */
    fun getUserData(): Any? {
        return Gson().fromJson<LoginResponseModel.ResponseObj>(
            Prefs.getString(CommonValues.USER_DATA, "Null"),
            LoginResponseModel.ResponseObj::class.java
        )
    }


}


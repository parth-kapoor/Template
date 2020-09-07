package code_setup.app_core

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import code_setup.app_util.CommonValues
import code_setup.app_util.callback_iface.SafeClickListener
import code_setup.app_util.di.AppComponent
import code_setup.app_util.location_utils.LocationTrackerService
import code_setup.app_util.location_utils.geo_locator.tracking.LocationTracker
import com.electrovese.setup.R

/**
 * Created by arischoice on 20/1/2019.
 */
abstract class CoreFragment() : androidx.fragment.app.Fragment() {


    /**
     * Activity  intents with bundel
     */
    fun activitySwitcher(from: FragmentActivity, to: Class<*>, bundle: Bundle?) {

        val intent = Intent(from, to)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        from.startActivity(intent)
        from.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("onRequestPermissionsResult :: ", "User permissions granted" + "")
    }

    fun checkAndRequestLocationPermissions(): Boolean {
        var locPermission: Int =
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_COARSE_LOCATION)
        var locFPermission: Int =
            ContextCompat.checkSelfPermission(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)

        val listPermissionsNeeded = java.util.ArrayList<String>()
        if (locPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if (locFPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                activity!!,
                listPermissionsNeeded.toArray(arrayOfNulls(listPermissionsNeeded.size)),
                CommonValues.REQUEST_CODE_PERMISSIONS_LOCATION
            )
            return false
        }

        return true
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onActivityInject()
    }

    /**
     * attach dagger with activity
     */
    protected abstract fun onActivityInject()
    fun getAppcomponent(): AppComponent = BaseApplication.appComponent


    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun registerLocationUpdateEvents() {
        LocationTracker.requestLocationUpdates(activity!!, LocationTrackerService::class.java)
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun removeLocationUpdates() {
        LocationTracker.removeLocationUpdates(activity!!)
    }

    fun TextView.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)

        }
        setOnClickListener(safeClickListener)
    }


    fun InitCall(
        num: String
    ) {
        val call = Intent(Intent.ACTION_CALL)
        call.data = Uri.parse("tel:" + num!!.toString())
        if (ActivityCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            applyPermission(activity!!, Manifest.permission.CALL_PHONE, 100)
            return
        }
        startActivity(call)
    }

    /**
     * 请求权限
     */
    //https://www.programcreek.com/java-api-examples/?class=android.content.Intent&method=ACTION_CALL
    fun applyPermission(activity: Activity, permission: String, requestCode: Int) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }
}
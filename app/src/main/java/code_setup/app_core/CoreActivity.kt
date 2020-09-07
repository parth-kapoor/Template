package code_setup.app_core

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.broadcasr_receiver.NetworkChangeReceiver
import code_setup.app_util.callback_iface.SafeClickListener
import code_setup.app_util.di.AppComponent
import code_setup.app_util.location_utils.LocationTrackerService
import code_setup.app_util.location_utils.geo_locator.tracking.LocationTracker
import code_setup.app_util.socket_work.SocketService
import com.base.mvp.BasePresenter
import com.base.mvp.BaseView
import com.electrovese.setup.R
import com.google.gson.Gson


/**
 * Created by arischoice on 20/1/2019.
 */
abstract class CoreActivity() : AppCompatActivity(), BaseView {

    override fun setPresenter(presenter: BasePresenter<*>) {
        this.presenter = presenter
    }

    companion object {
        lateinit var instance: CoreActivity
    }

    lateinit var mProgressDialog: ProgressDialog
    private var presenter: BasePresenter<*>? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        instance = this
        setContentView(bindUi())
        onActivityInject()
        initLoading()

    }
    /*
    * Camera nad Gallery permisison
    * */
    fun checkAndRequestPermissions(): Boolean {
        var camPermission: Int =
            ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        var readstoragePermission: Int =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        var writestoragePermission: Int =
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        val listPermissionsNeeded = java.util.ArrayList<String>()
        if (readstoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        if (writestoragePermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
        if (camPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA)

        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                listPermissionsNeeded.toArray(arrayOfNulls(listPermissionsNeeded.size)),
                CommonValues.REQUEST_CODE_PERMISSIONS_CAMERA
            )
            return false
        }
        return true
    }


    private var mNetworkReceiver: BroadcastReceiver? = null
    fun intNetworkListner() {
        mNetworkReceiver = NetworkChangeReceiver()
        registerNetworkBroadcastForNougat()
    }

    fun registerNetworkBroadcastForNougat() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            registerReceiver(
                mNetworkReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
        }
    }

    protected open fun unregisterNetworkChanges() {
        try {
            unregisterReceiver(mNetworkReceiver)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }



    /**
     * attach dagger with activity
     */
    protected abstract fun onActivityInject()

    fun getAppcomponent(): AppComponent = BaseApplication.appComponent
    private fun bindUi(): Int {
        return getScreenUi()
    }

    abstract fun getScreenUi(): Int


    override fun onDestroy() {
        super.onDestroy()
        presenter?.detachView()
        presenter = null
//        stopService(Intent(this, SocketService::class.java))
    }

    /**
     * initialise default loader
     */
    private fun initLoading() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog.setMessage("Loading")
        mProgressDialog.setCancelable(false)
        mProgressDialog.isIndeterminate = true
    }

    /**
     * show loading
     */
    fun showLoading() {
        if (!mProgressDialog.isShowing) {
            mProgressDialog.show()
        }
    }

    /**
     * hide Loading
     */
    fun closeLoading() {
        if (mProgressDialog.isShowing) {
            mProgressDialog.dismiss()
        }
    }

    /**
     * Activity  intents with bundel
     */
    fun activitySwitcher(from: Activity, to: Class<*>, bundle: Bundle?) {

        val intent = Intent(from, to)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        from.startActivity(intent)
        from.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    /**
     * Activity  intents with requestcode with bundel
     */
    fun activitySwitcherResult(from: Activity, to: Class<*>, bundle: Bundle?, requestCode: Int) {

        val intent = Intent(from, to)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        from.startActivityForResult(intent, requestCode)
        from.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    fun TextView.setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)

        }
        setOnClickListener(safeClickListener)
    }


    fun replaceContainer(
        portoFragment: Fragment,
        fragmentContainer: Int
    ) {
        if (portoFragment != null) {
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(R.anim.bottom_up, R.anim.bottom_down)
            fragmentTransaction.addToBackStack(portoFragment::class.java.simpleName)
            fragmentTransaction.replace(fragmentContainer, portoFragment)
            fragmentTransaction.commitAllowingStateLoss()
            // set the toolbar title
        }
    }

    fun removeFragment(
        frag: Fragment,
        bottomSheetContainer: Int
    ) {
//        getFragmentManager().beginTransaction().remove(getFragmentManager().findFragmentById(bottomSheetContainer)).commit();
        try {
            val oldFragment =
                supportFragmentManager.findFragmentById(bottomSheetContainer)
            if (oldFragment != null) {
                supportFragmentManager
                    .beginTransaction().remove(oldFragment).commit()
            }
        } catch (e: Exception) {
            e.printStackTrace()

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
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

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun registerLocationUpdateEvents() {
        LocationTracker.requestLocationUpdates(this, LocationTrackerService::class.java)
    }

    @RequiresPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun removeLocationUpdates() {
        LocationTracker.removeLocationUpdates(this)
    }

    open fun isFragmentInBackstack(
        fragmentManager: FragmentManager,
        fragmentTagName: String
    ): Boolean {
        for (entry in 0 until fragmentManager.getBackStackEntryCount()) {
            if (fragmentTagName == fragmentManager.getBackStackEntryAt(entry).getName()) {
                return true
            }
        }
        return false
    }

    fun InitCall(
        num: String
    ) {
        val call = Intent(Intent.ACTION_CALL)
        call.data = Uri.parse("tel:" + num!!.toString())
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            applyPermission(this, Manifest.permission.CALL_PHONE, 100)
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
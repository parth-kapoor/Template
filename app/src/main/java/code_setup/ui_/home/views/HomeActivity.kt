package code_setup.ui_.home.views

import android.Manifest
import android.animation.Animator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Paint
import android.graphics.drawable.AnimatedVectorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import code_setup.app_core.BaseApplication
import code_setup.app_core.CoreActivity
import code_setup.app_models.other_.ADLocation
import code_setup.app_models.other_.NotificationDataBodyModel
import code_setup.app_models.other_.NotificationModel
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_models.response_.CurrentStatusResponseModel
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_models.response_.UpdateStatusResponseModel
import code_setup.app_util.*
import code_setup.app_util.callback_iface.OnBottomDialogItemListener
import code_setup.app_util.direction_utils.*
import code_setup.app_util.location_utils.MyTracker
import code_setup.db_.AppDatabase
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkConstant
import code_setup.net_.NetworkRequest
import code_setup.ui_.auth.views.authantication_.LoginActNew
import code_setup.ui_.home.di_home.DaggerHomeComponent
import code_setup.ui_.home.di_home.HomeModule
import code_setup.ui_.home.home_mvp.HomePresenter
import code_setup.ui_.home.home_mvp.HomeView
import code_setup.ui_.home.views.fragments.BookingFragment
import code_setup.ui_.home.views.fragments.DeliveryUpdatesFragment
import code_setup.ui_.home.views.fragments.TripUpdatesFragment
import code_setup.ui_.home.views.ride.SelectRouteActivity
import code_setup.ui_.settings.NotificationScreen
import code_setup.ui_.settings.ProfileActivity
import code_setup.ui_.settings.views.about.AboutFragment
import code_setup.ui_.settings.views.ridehistory.RideHistoryActivity
import code_setup.ui_.settings.views.support.SupportActivity
import com.base.mvp.BasePresenter
import com.birjuvachhani.locus.Locus
import com.electrovese.setup.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.navigation.NavigationView
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.default_loading.*
import kotlinx.android.synthetic.main.home_status_notify_view.*
import kotlinx.android.synthetic.main.home_top_custom_view.*
import kotlinx.android.synthetic.main.layout_botttom_status_view.*
import kotlinx.android.synthetic.main.layout_side_drawer.*
import kotlinx.android.synthetic.main.nav_header_main.*
import kotlinx.android.synthetic.main.profile_top_view.*
import kotlinx.android.synthetic.main.trans_toolbar_lay.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class HomeActivity : CoreActivity(), NavigationView.OnNavigationItemSelectedListener, HomeView,
    OnMapReadyCallback, MyTracker.ADLocationListener {
    override fun whereIAM(loc: ADLocation?) {

    }

    private var showRevelAnimation: Boolean = false
    lateinit var mRunnable: Runnable
    lateinit var mHandler: Handler
    private var markersCurrentLication: Marker? = null
    lateinit var handler: Handler
    private lateinit var upcomingBottomSheet: UpcomingBottomSheet
    private lateinit var mapFragment: SupportMapFragment
    var isOpened: Boolean = false
    var TAG: String = HomeActivity::class.java.simpleName
    private var mDefaultLocation: LatLng? = null
    private lateinit var mMap: GoogleMap
    var locationUpdatesLocked = false

    companion object {
        lateinit var homeInstance: HomeActivity
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onResume() {
        super.onResume()
        requestLocationPermissions()
    }

    @SuppressLint("MissingPermission")
    override fun onResponse(list: Any, int: Int) {
        Log.e(TAG, "" + Gson().toJson(list))

        when (int) {
            NetworkRequest.REQUEST_CHANGE_STATUS -> {
                var responseData = list as UpdateStatusResponseModel
//                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
//                    removeLocationUpdates()
//                } else {
//                    AppUtils.showSnackBar(this, getString(R.string.error_session_expired))
//                    logoutUser()
//                }
            }
            NetworkRequest.REQUEST_CURRENT_STATUS -> {
                var responseData = list as CurrentStatusResponseModel
                /* if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {

                     if (responseData.response_obj != null && responseData.response_obj.order_id.isNotEmpty()) {
                         Prefs.putString(
                             CommonValues.CURRENT_ORDER_ID,
                             responseData.response_obj.order_id
                         )
                         registerLocationUpdateEvents()
                         viewModes(
                             VIEW_MODE.BOTTOM_FRAGMENT.nCodes,
                             DeliveryUpdatesFragment()
                         )
                         Handler().postDelayed(Runnable {
                             EventBus.getDefault().postSticky(
                                 CustomEvent<Any>(
                                     EVENTS.REQUEST_ORDER_DETAIL,
                                     responseData.response_obj.order_id
                                 )
                             )
                         }, 100)
                     } else {
                         Prefs.putString(CommonValues.CURRENT_ORDER_ID, "")
                         removeLocationUpdates()
                         if (responseData.response_obj.notification_data != null) {
                             newJobReceived(responseData.response_obj.notification_data, 1)
                         }
                     }
                 } else {
                     Prefs.putString(CommonValues.CURRENT_ORDER_ID, "")
                     removeLocationUpdates()
                     AppUtils.showSnackBar(this, getString(R.string.error_session_expired))
                     logoutUser()
                 }*/
            }
            NetworkRequest.REQUEST_LOGOUT_USER -> {
//                activitySwitcher(this, LoginActivity::class.java, null)
//                Prefs.clear()
//                finishAffinity()
            }
        }


    }

    private fun showBottomView(showStatus: Boolean) {
        if (showStatus) {
            AnimUtils.moveAnimationY(startbookingBottomView, true, 1000)
        } else {
            AnimUtils.moveAnimationY(startbookingBottomView, false, 100)
        }

    }


    override fun showProgress() {
        homeLoaderView.show()
    }

    override fun hideProgress() {
        homeLoaderView.hide()
    }

    override fun noResult() {

    }

    @Inject
    lateinit var presenter: HomePresenter
    var avd: AnimatedVectorDrawable? = null

    override fun onActivityInject() {
        DaggerHomeComponent.builder().appComponent(getAppcomponent())
            .homeModule(HomeModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    override fun getScreenUi(): Int {
        return R.layout.activity_main
    }

    override fun onError() {

    }

    override fun setPresenter(presenter: BasePresenter<*>) {

    }

    lateinit var action: Runnable

    private lateinit var addDatabase: AppDatabase
    override fun onDestroy() {
        AppDatabase.destroyInstance()
        unregisterNetworkChanges();
        super.onDestroy()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeInstance = this
//        loadingView.visibility = View.VISIBLE
        try {
            wishMessageText.text =
                presenter.getWishTextMessage() + " " + (getUserData() as LoginResponseModel.ResponseObj).name
        } catch (e: Exception) {
        }
        AppUtils.handleonScreenBottomNavigation(this)
        toolbar_root.bringToFront()
        setupsideMenuDetail()
        nav_view.setNavigationItemSelectedListener(this)
        clickCalls()
//      captureInfo()

        mapSetup()
        Handler().postDelayed(Runnable {
            progress_loading.visibility = View.GONE
        }, 3000)

        getIntentData()
//        getCurrentStatus()
//        connectToSocket()

        intNetworkListner()
        // THIS ONLY FOR TESTING PURPOSE------------
//        showNewRequestDialog(CommonValues.notificationModel)

//        Handler().postDelayed(Runnable {
//            viewModes(VIEW_MODE.BOTTOM_FRAGMENT.nCodes, TripUpdatesFragment())
//        }, 3000)
    }


    var mSocket: Socket? = null
    private fun connectToSocket() {
        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.query = "token=" + Prefs.getString(CommonValues.ACCESS_TOKEN, "")
            mSocket = IO.socket(removeLastChar(NetworkConstant.BASE_URL_SOCKET), opts)
//          mSocket = IO.socket("http://91.205.173.97:6161", opts)
//            mSocket!!.connected()
//            mSocket!!.on(EVENTS.NEW_JOB, getSocketMessage)


//            mSocket!!.on(Socket.EVENT_CONNECT, onConnect);
//            mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect);
//            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//            mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//            mSocket!!.on(Socket.EVENT_ERROR, onError);


            mSocket!!.connect()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun removeLastChar(str: String): String {
        return str.substring(0, str.length - 1)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val getSocketMessage = Emitter.Listener { args ->
        Log.d(TAG, "getSocketMessage  " + args[0])
//        Log.d("GET_MESSAGE_Socket: ", " 2  " + Gson().toJson(args))

//        var updatesModel = Gson().fromJson("" + args[0], SocketUpdateModel::class.java)
//        Log.d("SocketService : ", "   " + Gson().toJson(updatesModel.message.data))
//        when (updatesModel.message.action_code) {
//            EVENTS.NEW_TOUR_JOB -> {
//                EventBus.getDefault()
//                    .postSticky(CustomEvent<Any>(EVENTS.SOCKET_REQUEST_NEW_TOUR_JOB, updatesModel))
//            }
//        }
//        return@Listener
    }


    private fun captureInfo() {
        presenter.captureInfo(NetworkRequest.REQUEST_CAPTURE_INFO, AppUtils.getCaptureInfoData())
    }

    private fun getIntentData() {
/*
        if (intent.getBooleanExtra(CommonValues.IS_FROM_NOTIFICATION, false)) {
            var notificatiobId = intent.getIntExtra(CommonValues.NOTIFICATION_ID, 1)
            var notificationDAta = intent.getSerializableExtra(CommonValues.TOUR_DATA)
            var nData = notificationDAta as NotificationModel
            val function = {
                var dataBody = Gson().fromJson<NotificationDataBodyModel>(
                    nData.data.toString(),
                    NotificationDataBodyModel::class.java
                )
                newJobReceived(nData,0)
            }
            Handler().postDelayed(
                Runnable(function),
                1000
            )
        }*/
    }

    private fun getCurrentStatus() {
        presenter.getCurrentStatus(NetworkRequest.REQUEST_CURRENT_STATUS)
    }

    /**
     * Update user sidemenu Detail
     */
    fun setupsideMenuDetail() {
        if (getUserData() != null) {
            var userData = getUserData() as LoginResponseModel.ResponseObj
            userImageView.setImageURI(userData.user_image)
            accountName.setText(userData.name)
        }
    }

    /**
     * Reset  view to home screen
     */
    fun backToHomeView() {
        BaseApplication.instance.CURRENT_VIEW_HOME = 0
        routingInProcess = false
        isBoundViewAdjusted = false
        BaseApplication.scheduledDate = ""
        back_toolbar.setImageResource(R.mipmap.ic_menu)
        homeMenuBtn.setImageResource(R.mipmap.ic_menu_white_bg)
        homeMenuBtn.visibility = View.VISIBLE
        homeTopBar.visibility = View.VISIBLE
        bookingSelectionLayout.visibility = View.VISIBLE
        showToolbar(false)
        hideTopStatusView()
        drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);
//      AnimUtils.homeAnimationX(back_toolbar, false)
//      txtToolbartitle.setText(R.string.str_home)
//      txtToolbartitle.setTextColor(resources.getColor(R.color.colorBlack))

        fragmentContainer.visibility = View.GONE
        mainView.visibility = View.VISIBLE
        removeFragment(DeliveryUpdatesFragment(), R.id.bottomSheetContainer)
        getCurrentStatus()
        defaultCameraFocus()
        showBottomView(true)
    }

    private fun showToolbar(b: Boolean) {
        if (b) toolbar_root.visibility = View.VISIBLE else toolbar_root.visibility = View.GONE
    }


    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun requestLocationPermissions() {
        Locus.getCurrentLocation(this) { result ->
            result.location?.let {
                /* Received location update */
                val latLng = LatLng(result.location!!.latitude, result.location!!.longitude)
                mDefaultLocation = latLng
                goNext()
                Log.e("onSuccess", " LOCATION PERMISSION GRANTED  ")
                Handler().postDelayed(Runnable {
                    locationUpdatesLocked = true
                    removeLocationUpdates()
                }, 15000)
            }
            result.error?.let {
                /* Received error! */
                Log.e("onFailed", " LOCATION PERMISSION FAILED  ")
                /*AppDialogs.openLocationPermissionAlert(
                    this,
                    Any(),
                    Any(),
                    object : OnBottomDialogItemListener<Any> {
                        override fun onItemClick(view: View, position: Int, type: Int, t: Any) {
//                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        }

                    })*/
            }
        }

    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun goNext() {
        if (!locationUpdatesLocked)
            registerLocationUpdateEvents()
        MyTracker(getApplicationContext(), this).track()
        Handler().postDelayed({
            if (!showRevelAnimation) {
                AnimUtils.startRevealAnimation(framLayoutMain, rootView)
                showRevelAnimation = true
                homeTopBar.bringToFront()
                showBottomView(true)
            }
        }, 100)
        defaultCameraFocus()
    }

    private val DEFAULT_ZOOM: Float = 14f
    @SuppressLint("MissingPermission")
    private fun defaultCameraFocus() {
        if (routingInProcess) {
            return
        }
        try {
            mMap.clear()
        } catch (e: Exception) {
        }
        if (mDefaultLocation == null) {
            val myLocation = AppUtils.getMyLocation()
            mDefaultLocation = myLocation
        }
        if (mDefaultLocation != null) {
            val position = CameraPosition.Builder()
                .target(mDefaultLocation)
                .zoom(DEFAULT_ZOOM)
                .build()
//              mMap.moveCamera(CameraUpdateFactory.newCameraPosition(position))
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(position))
            mylocationMarker = mMap.addMarker(
                MarkerOptions().position(mDefaultLocation!!).flat(true).icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_navigation)
                ).anchor(0.5f, 0.5f)
            )
//            mMap.setMyLocationEnabled(true)
            mMap.uiSettings.setRotateGesturesEnabled(false)
            mMap.uiSettings.isTiltGesturesEnabled = false
            mMap.uiSettings.setMyLocationButtonEnabled(true)

            if (mapFragment != null &&
                mapFragment.view!!.findViewById<View>(Integer.parseInt("1")) != null
            ) {
                val locationButton =
                    (mapFragment.view!!.findViewById<View>(Integer.parseInt("1")).parent as View).findViewById<View>(
                        Integer.parseInt(
                            "2"
                        )
                    )
                val rlp = locationButton.layoutParams as (RelativeLayout.LayoutParams)
                // position on right bottom
                rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
                rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE)
                rlp.setMargins(0, 0, 30, 150)
            }
        } else {
            mDefaultLocation = AppUtils.getMyLocation()
        }
    }

    private fun mapSetup() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        presenter.customMap(this@HomeActivity, mMap)
        defaultCameraFocus()

    }

    private fun clickCalls() {
        back_toolbar.setOnClickListener {
            backToHomeView()
        }

        homeMenuBtn.setOnClickListener {
            if (BaseApplication.instance.CURRENT_VIEW_HOME == 1) {
                backToHomeView()
            } else if (BaseApplication.instance.CURRENT_VIEW_HOME == 2) {
                backToHomeView()
            } else {
                if (isOpened) {
                    closeDrawer()
                } else
                    openDrawer()
            }

        }

        whereToTxtViewHome.setOnClickListener {
            val intent = android.content.Intent(
                this@HomeActivity,
                SelectRouteActivity::class.java
            )
//            intent.putExtra(CommonValues.Project_ID, t as String)
//                        intent.putExtra(
//                            CommonValues.Project_DATA,
//                            featuredAdapter.getselctedPosition(position)
//                        )
            val options =
                androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                    this@HomeActivity,
                    whereToTxtViewHome!!, whereToTxtViewHome.transitionName
                )
            startActivityForResult(
                intent,
                CommonValues.REQUEST_CODE_LOCATION_PICKER,
                options.toBundle()
            )
        }

        scheduleRideTxtBtn.setPaintFlags(scheduleRideTxtBtn.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)
        scheduleRideTxtBtn.setOnClickListener {
            AppDialogs.openDialogScheduleBooking(
                this@HomeActivity,
                Any(),
                Any(),
                object : OnBottomDialogItemListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any) {
                        var bndl = Bundle()
                        bndl.putString(CommonValues.SCHEDULED_DATE, t as String)
                        activitySwitcherResult(
                            this@HomeActivity, SelectRouteActivity::class.java, bndl,
                            CommonValues.REQUEST_CODE_LOCATION_PICKER
                        )
                    }
                })
        }


        homeBooktaxiBtn.setOnClickListener {
            changeBookingType(0)
        }
        val function: (View) -> Unit = {
            changeBookingType(1)
        }
        homeSharetaxiBtn.setOnClickListener(function)





        menuyourRideHistoryBtn.setSafeOnClickListener {
            closeDrawer()
            activitySwitcher(this, RideHistoryActivity::class.java, null)
        }
        /*------*/
        menuAbutBtn.setSafeOnClickListener {
            viewModes(VIEW_MODE.FRAGMENT.nCodes, AboutFragment())

        }
        menuNotificationBtn.setOnClickListener {
            closeDrawer()
            activitySwitcher(this, NotificationScreen::class.java, null)
        }

        topViewdrawerHeaderProfile.setOnClickListener {
            closeDrawer()
            activitySwitcher(this, ProfileActivity::class.java, null)
        }
        menuSuptBtn.setOnClickListener {
            closeDrawer()
            activitySwitcher(this, SupportActivity::class.java, null)
        }
        menuProfileBtn.setOnClickListener {
            closeDrawer()
            activitySwitcher(this, ProfileActivity::class.java, null)
        }
        menuLogoutBtn.setOnClickListener {
            logoutUser()
//            if (!Prefs.getString(CommonValues.CURRENT_ORDER_ID, "")!!.isNotEmpty()) {
//                logoutUser()
//            } else {
//                AppUtils.showSnackBar(this, getString(R.string.strMessage_youcannot_logout))
//            }

        }


    }

    private fun changeBookingType(iType: Int) {
        when (iType) {
            0 -> {
                homeBooktaxiBtn.setBackgroundResource(R.drawable.rectangle_background)
                homeBooktaxiBtn.setTextColor(resources.getColor(R.color.colorWhite))

                homeSharetaxiBtn.setBackgroundResource(R.drawable.rectangle_background_white)
                homeSharetaxiBtn.setTextColor(resources.getColor(R.color.colorTextGrey1))
                BaseApplication.bookingType = CommonValues.NORMAL_BOOKING_TYPE
            }
            1 -> {
                homeSharetaxiBtn.setBackgroundResource(R.drawable.rectangle_background)
                homeSharetaxiBtn.setTextColor(resources.getColor(R.color.colorWhite))

                homeBooktaxiBtn.setBackgroundResource(R.drawable.rectangle_background_white)
                homeBooktaxiBtn.setTextColor(resources.getColor(R.color.colorTextGrey1))
                BaseApplication.bookingType = CommonValues.SHARE_BOOKING_TYPE
            }

        }
    }

    private fun logoutUser() {
        activitySwitcher(this, LoginActNew::class.java, null)
        Prefs.clear()
        finishAffinity()
//        presenter.logoutUser(NetworkRequest.REQUEST_LOGOUT_USER)
    }

    fun viewModes(fMode: Int, aFragment: Fragment) {
        BaseApplication.instance.CURRENT_VIEW_HOME = fMode
        when (fMode) {
            VIEW_MODE.FRAGMENT.nCodes -> {
                Log.d("view mode ", " fragments")
                homeTopBar.visibility = View.GONE
                showToolbar(true)
                hideTopStatusView()
                back_toolbar.setImageResource(R.mipmap.ic_back_black)
//              AnimUtils.homeAnimationX(back_toolbar, false)
                mainView.visibility = View.GONE
                fragmentContainer.visibility = View.VISIBLE
                bottomSheetContainer.visibility = View.GONE
                removeFragment(DeliveryUpdatesFragment(), R.id.fragmentContainer)
                replaceContainer(aFragment, R.id.fragmentContainer)
                closeDrawer()
                progress_loading.visibility = View.GONE
            }
            VIEW_MODE.BOTTOM_FRAGMENT.nCodes -> {
                Log.d("view mode ", " bottom fragments")
                closeDrawer()
                showBottomView(false)
                fragmentContainer.visibility = View.GONE
                bottomSheetContainer.visibility = View.VISIBLE
                bottomSheetContainer.bringToFront()
                replaceContainer(aFragment, R.id.bottomSheetContainer)
                progress_loading.visibility = View.GONE
                mMap.setPadding(0, 0, 0, 500)
                homeTopBar.visibility = View.VISIBLE
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }

            VIEW_MODE.ACTIVITY.nCodes -> {
                backToHomeView()
                AnimUtils.homeAnimationX1(back_toolbar, true)
                removeFragment(DeliveryUpdatesFragment(), R.id.bottomSheetContainer)
                drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED);

            }
            VIEW_MODE.LOADING.nCodes -> {
                removeFragment(DeliveryUpdatesFragment(), R.id.bottomSheetContainer)
                loadingView.visibility = View.VISIBLE
                Handler().postDelayed(Runnable {
                    loadingView.visibility = View.GONE
                    viewModes(VIEW_MODE.BOTTOM_FRAGMENT.nCodes, TripUpdatesFragment())
                }, 3000)
                mMap.setPadding(0, 0, 0, 300)

            }
        }
    }

    private fun closeDrawer() {
        isOpened = false
        drawer_layout.closeDrawer(GravityCompat.START)
    }

    private fun openDrawer() {
        isOpened = true
        drawer_layout.openDrawer(GravityCompat.START)
    }


    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            closeDrawer()
            routingInProcess = false
            isBoundViewAdjusted = false
//            back_toolbar.setImageResource(R.mipmap.ic_menu)
            homeMenuBtn.setImageResource(R.mipmap.ic_menu_white_bg)
            bookingSelectionLayout.visibility = View.VISIBLE
            homeTopBar.visibility = View.VISIBLE
            homeMenuBtn.visibility = View.VISIBLE
            showToolbar(false)
            hideTopStatusView()
            fragmentContainer.visibility = View.GONE
            mainView.visibility = View.VISIBLE
            when (BaseApplication.instance.CURRENT_VIEW_HOME) {
                VIEW_MODE.BOTTOM_FRAGMENT.nCodes -> {
                    viewModes(VIEW_MODE.ACTIVITY.nCodes, Fragment())
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessage(event: CustomEvent<Any>) {
        Log.d("onMessage", TAG + event.type)
        try {
            Log.d("onMessage", TAG + event.oj.toString())
        } catch (e: Exception) {
        }
        when (event.type) {
            EVENTS.EVENT_NEW_JOB -> {
                newJobReceived(event.oj, 0)
            }
            EVENTS.NETWORK_CONNECTION_CALLBACK -> {
                if (event.oj as Boolean) {
                    AppUtils.showSnackBarConnectiivity(
                        this,
                        getString(R.string.str_connected_to_internet),
                        true
                    )
                } else {
                    AppUtils.showSnackBarConnectiivity(
                        this,
                        getString(R.string.str_no_internet_conection),
                        false
                    )
                }

            }

            EVENTS.EVENT_BOOKING_SCHEDULED -> {
                backToHomeView()
            }

            EVENTS.CURRENT_LOCATION -> {
                try {
                    presenter.fetchLocation(this@HomeActivity,
                        Prefs.getDouble(CommonValues.LATITUDE, 0.0),
                        Prefs.getDouble(CommonValues.LONGITUDE, 0.0),
                        MyTracker.ADLocationListener {
                            Log.d(TAG, "Current Location " + it.address)
                            Prefs.putString(CommonValues.CURRENT_ADDRESS, it.address)
                        })
                } catch (e: Exception) {
                }
            }
        }
        removeStickyEvent()
    }

    private fun newJobReceived(oj: Any, iCode: Int) {
        viewModes(
            VIEW_MODE.BOTTOM_FRAGMENT.nCodes,
            DeliveryUpdatesFragment()
        )
        Handler().postDelayed(Runnable {
            when (iCode) {
                0 -> {
                    EventBus.getDefault().postSticky(
                        CustomEvent<Any>(
                            EVENTS.REQUEST_NEW_DELIVERY_JOB,
                            oj as NotificationModel
                        )
                    )
                }
                1 -> {
                    EventBus.getDefault().postSticky(
                        CustomEvent<Any>(
                            EVENTS.REQUEST_NEW_DELIVERY_JOB_FROM_API,
                            oj as NotificationDataBodyModel
                        )
                    )
                }
            }

        }, 100)
    }

    private fun removeStickyEvent() {
        var stickyEvent = EventBus.getDefault().getStickyEvent(CustomEvent::class.java)
// Better check that an event was actually posted before
        if (stickyEvent != null) {
            // "Consume" the sticky event
            EventBus.getDefault().removeStickyEvent(stickyEvent);
            // Now do something with it
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        // menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CommonValues.REQUEST_CODE_LOCATION_PICKER -> {
                try {
                    if (BaseApplication.selectedMyLocation != null && BaseApplication.selectedDestinationLocation != null) {
                        Log.e(
                            "onActivityResult",
                            "1 " + Gson().toJson(BaseApplication.selectedMyLocation)
                        )
                        Log.e(
                            "onActivityResult",
                            "2 " + Gson().toJson(BaseApplication.selectedDestinationLocation)
                        )

                        viewModes(VIEW_MODE.BOTTOM_FRAGMENT.nCodes, BookingFragment())
                        Handler().postDelayed(Runnable {
                            EventBus.getDefault().postSticky(
                                CustomEvent<Any>(
                                    EVENTS.NEW_TAXI_REQUEST_CONFIRM_BOOKING,
                                    true
                                )
                            )
                        }, 100)
                    }
                } catch (e: Exception) {
                }

            }
        }
    }

    fun showBackArrow() {
        homeMenuBtn.setImageResource(R.mipmap.ic_back_white_bg)
        bookingSelectionLayout.visibility = View.GONE
    }

    fun showTopStatusView() {
        homeTopStatusViewHolder.bringToFront()
        homeTopStatusViewHolder.visibility = View.VISIBLE
    }

    fun hideTopStatusView() {
        homeTopStatusViewHolder.bringToFront()
        homeTopStatusViewHolder.visibility = View.GONE
    }

    fun changeViewFocus(b: Boolean) {
        if (b) {
            homeTopStatusViewHolder.visibility = View.GONE
            homeMenuBtn.visibility = View.GONE
        } else {
            homeTopStatusViewHolder.visibility = View.VISIBLE
            homeMenuBtn.visibility = View.VISIBLE
        }
    }

    var destinationLocationMarker: Marker? = null
    var mylocationMarker: Marker? = null
    var destinationLocation: LatLng? = null
    var myLocation: LatLng? = null
    private var isBoundViewAdjusted: Boolean = false
    var routingInProcess: Boolean = false
    val directions: Directions by lazy {
        DirectionsSdk("ADD YOUR KEY HERE")
    }

    fun showtourRoute(latLngMy: LatLng, latLngDest: LatLng) {
        routingInProcess = true
        if (mMap != null) {
            mMap.clear()
        }
        myLocation = LatLng(latLngMy.latitude, latLngMy.longitude)
        if (mylocationMarker != null)
            mylocationMarker!!.remove()
        mylocationMarker = presenter.getMyMarker(mMap, myLocation!!, "My Location")

        destinationLocation = LatLng(latLngDest.latitude, latLngDest.longitude)
        if (destinationLocationMarker != null)
            destinationLocationMarker!!.remove()
        destinationLocationMarker =
            presenter.getDestinationMarker(mMap, destinationLocation!!, "Destination Location")

        Handler(Looper.getMainLooper()).post(
            Runnable {
                if (!isBoundViewAdjusted) {
                    showAllMarkersAndGetDirections(destinationLocation!!, myLocation)
                    isBoundViewAdjusted = true
                }
            })
    }

    private fun showAllMarkersAndGetDirections(
        fstMarker: LatLng?,
        secondMarker: LatLng?
    ) {
        if (fstMarker != null && secondMarker != null) {
            val width = resources.displayMetrics.widthPixels
            val padding = (width * 0.10).toInt() //
            val cu = CameraUpdateFactory.newLatLngBounds(getLatLngBoundsForMarkers(), padding)
            mMap.moveCamera(cu)
            mMap.animateCamera(cu)
            Handler().postDelayed({
                getMapDirectionMarkers(
                    fstMarker!!,
                    secondMarker!!
                )
            }, 100)

        }
    }

    internal fun getLatLngBoundsForMarkers(): LatLngBounds {
        val builder = LatLngBounds.Builder()
        builder.include(mDefaultLocation)
        if (myLocation != null) {
            builder.include(myLocation)
        }
        if (destinationLocation != null) {
            builder.include(destinationLocation)
        }
        return builder.build()
    }

    private fun getMapDirectionMarkers(dDestionation: LatLng, dOrigin: LatLng) {
        directions.getSuggestions(
            origin = Position(dOrigin.latitude, dOrigin.longitude),
            destination = Position(dDestionation.latitude, dDestionation.longitude)
        ).observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::handleSuccessUpdate, this::handleFaliurUpdate)
    }

    private fun handleSuccessUpdate(baseResponse: GeocodedResponse) {
        Log.e(" SuccessUpdate ", "  " + Gson().toJson(baseResponse))
        if (baseResponse.routes != null && baseResponse.routes.size > 0) {
//            isDirection = true
            drawPolyLine(presenter.getModifiedList(baseResponse))
        }

    }

    private var mPrimaryPolyLineColor: Int = 0
    private var mSecondaryPolyLineColor: Int = 0
    private var mPrimaryPolyLine: Polyline? = null
    private var mSecondaryPolyLine: Polyline? = null

    private fun drawPolyLine(routes: ArrayList<Route>) {
        val lineOptions = PolylineOptions()

        lineOptions.width(10f)
        if (mPrimaryPolyLineColor == 0) {
            lineOptions.color(R.color.colorBlack)
        } else {
            lineOptions.color(R.color.colorBlack)
//          lineOptions.color(ContextCompat.getColor(this@HomeActivity!!, mPrimaryPolyLineColor))
        }
        lineOptions.startCap(RoundCap())
        lineOptions.endCap(RoundCap())
        // lineOptions.endCap(CustomCap(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_destination), 10f))
        lineOptions.jointType(JointType.BEVEL)
        mPrimaryPolyLine = mMap.addPolyline(lineOptions)

        val greyOptions = PolylineOptions()
        greyOptions.width(10f)
        if (mSecondaryPolyLineColor == 0) {
            greyOptions.color(resources.getColor(R.color.colorBlack))
        } else {
            lineOptions.color(
                ContextCompat.getColor(
                    this@HomeActivity!!,
                    mSecondaryPolyLineColor
                )
            )
        }
        greyOptions.startCap(SquareCap())
        greyOptions.endCap(SquareCap())
        greyOptions.jointType(JointType.ROUND)
        mSecondaryPolyLine = mMap.addPolyline(greyOptions)
        animatePolyLine(routes)
    }

    var routeLatLong = ArrayList<LatLng>()
    private fun animatePolyLine(routes: ArrayList<Route>) {
        routeLatLong = ArrayList<LatLng>()
        routeLatLong.addAll(routes.get(0).points!!)
        val animator = ValueAnimator.ofInt(0, 100)
        animator.duration = 1000
        animator.interpolator = LinearInterpolator()
        animator.addUpdateListener { animator ->
            val latLngList = mPrimaryPolyLine!!.getPoints()
            val initialPointSize = latLngList.size
            val animatedValue = animator.animatedValue as Int
            val newPoints = animatedValue * routeLatLong!!.size / 100

            if (initialPointSize < newPoints) {
                latLngList.addAll(routeLatLong!!.subList(initialPointSize, newPoints))
                mPrimaryPolyLine!!.setPoints(latLngList)

            }
        }

        animator.addListener(polyLineAnimationListener)
        animator.start()


    }

    internal var polyLineAnimationListener: Animator.AnimatorListener =
        object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {

            }

            override fun onAnimationEnd(animator: Animator) {

                val primaryLatLng = mPrimaryPolyLine!!.getPoints()
                val secondaryLatLng = mSecondaryPolyLine!!.getPoints()

                secondaryLatLng.clear()
                secondaryLatLng.addAll(primaryLatLng)
                primaryLatLng.clear()

                mPrimaryPolyLine!!.setPoints(primaryLatLng)
                mSecondaryPolyLine!!.setPoints(secondaryLatLng)

                mPrimaryPolyLine!!.setZIndex(2f)

//            animator.start()
            }

            override fun onAnimationCancel(animator: Animator) {

            }

            override fun onAnimationRepeat(animator: Animator) {


            }
        }

    private fun handleFaliurUpdate(error: Throwable) {
        Log.e(" faliurUpdate ", "  " + error.message)
    }
}

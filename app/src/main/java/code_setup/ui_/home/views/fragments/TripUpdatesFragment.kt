package code_setup.ui_.home.views.fragments

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.annotation.RequiresApi
import code_setup.app_core.CoreFragment
import code_setup.app_models.other_.ADLocation
import code_setup.app_models.other_.NotificationDataBodyModel
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_models.request_.OrderDetailRequestModel
import code_setup.app_models.request_.UpdateOrderRequestModel
import code_setup.app_models.response_.BaseResponseModel
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.*
import code_setup.app_util.callback_iface.OnBottomDialogItemListener
import code_setup.app_util.location_utils.MyTracker
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.home.di_home.DaggerHomeComponent
import code_setup.ui_.home.di_home.HomeModule
import code_setup.ui_.home.home_mvp.HomePresenter
import code_setup.ui_.home.home_mvp.HomeView
import code_setup.ui_.home.views.HomeActivity
import code_setup.ui_.home.views.rate.RateTripActivity
import com.base.mvp.BasePresenter
import com.electrovese.setup.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_tour_updates_layout.*
import kotlinx.android.synthetic.main.fragment_trip_updates_layout.*
import kotlinx.android.synthetic.main.layout_delivery_updates_top_view.*
import kotlinx.android.synthetic.main.layout_new_request_view.*
import kotlinx.android.synthetic.main.layout_reach_for_pickup.*
import kotlinx.android.synthetic.main.layout_ride_detail_view.*
import kotlinx.android.synthetic.main.layout_root_view.*
import kotlinx.android.synthetic.main.trans_toolbar_lay.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class TripUpdatesFragment : CoreFragment(), HomeView, MyTracker.ADLocationListener {

    private var fullViewVisible: Boolean = false
    private var jobTimerCountdown: Long = 60000// default value 60 sec
    lateinit var orderID: String
    var TAG: String = TripUpdatesFragment::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    /*
    *  E V E N T  BUS
    *  Update Viewe as per respected event
    *  @ Send custom object @CustomEvent
    * */
    @SuppressLint("RestrictedApi")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onMessage(event: CustomEvent<Any>) {
        Log.d(TAG, "onMessage      " + event.type)
        when (event.type) {
            EVENTS.REQUEST_ORDER_DETAIL -> {
//                orderID = event.oj as String
//                getOrderDetail()
            }
        }
    }


    private fun navigationIntent(
        latLng: LatLng,
        name: String
    ) {
        val strUri =
            "http://maps.google.com/maps?q=loc:" + latLng.latitude.toString() + "," + latLng.longitude.toString() + " (" + name.toString() + ")"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(strUri))
        intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity")
        startActivity(intent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var vw: View = inflater.inflate(R.layout.fragment_trip_updates_layout, container, false)
        // Inflate the layout for this fragment
//        changeGravityToBottom(vw)
        return vw
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermissions()
        try {
//            HomeActivity.homeInstance.txtToolbartitle.setText(R.string.str_delivery_status)
            HomeActivity.homeInstance.showBackArrow()
            HomeActivity.homeInstance.showTopStatusView()
        } catch (e: Exception) {
        }


        topArrowTouchListner()
        cancelRideBtn.setOnClickListener {
            HomeActivity.homeInstance.backToHomeView()
        }

        Handler().postDelayed(Runnable {
            showOnRideView()
        }, 3000)

        endTripBtn.setOnClickListener {
            activitySwitcher(activity!!, RateTripActivity::class.java, null)
        }
        sosBtn.setOnClickListener {
            AppDialogs.openDialogSoSAlert(
                activity!!,
                Any(),
                Any(),
                object : OnBottomDialogItemListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any) {

                    }
                })
        }

    }

    private fun showOnRideView() {
        emergencyBtnsHolder.visibility = View.VISIBLE
        sosBtn.visibility = View.VISIBLE
        rideStatusText.setText(R.string.str_onride)
        driverStatusTopTextView.setText("")
    }

    private fun topArrowTouchListner() {
        topArrowTripView.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if (!fullViewVisible)
                            showFullView()
                        else {
                            hideFullView()
                        }
                    }
                }

                return v?.onTouchEvent(event) ?: true
            }
        })
    }

    private fun hideFullView() {
        fullViewVisible = false
//        ride_detail_view_holder.visibility = View.GONE
//        AnimUtils.SlideToDown(ride_detail_view_holder)
//        ride_detail_view_holder.animate().scaleY(1f).setInterpolator(AccelerateDecelerateInterpolator()).setDuration(1000);
        val anim = ValueAnimator.ofInt(ride_detail_view_holder.getMeasuredHeight(), 70);
        anim.addUpdateListener(ValueAnimator.AnimatorUpdateListener() {

            val val1 = it.getAnimatedValue()
            Log.e("anim to hide", " " + val1);
            val layoutParams = ride_detail_view_holder.getLayoutParams();
            layoutParams.height = val1.toString().toInt();
            ride_detail_view_holder.setLayoutParams(layoutParams);
            if (val1.toString().toInt() < screenHeight / 2) {
                floatBtnHolder.visibility = View.VISIBLE

                driverStatusTopTextView.visibility = View.GONE
                lineViewSlide.visibility = View.VISIBLE
                HomeActivity.homeInstance.changeViewFocus(false)
            }
        });
        anim.setDuration(800);
        anim.start();
    }

    private fun showFullView() {
        fullViewVisible = true
//        AnimUtils.SlideToAbove(ride_detail_view_holder)
//        ride_detail_view_holder.visibility = View.VISIBLE

        val anim = ValueAnimator.ofInt(ride_detail_view_holder.getMeasuredHeight(), screenHeight);
        anim.addUpdateListener(ValueAnimator.AnimatorUpdateListener() {
            val val1 = it.getAnimatedValue()
            Log.e("anim to full", " " + val1);
            val layoutParams = ride_detail_view_holder.getLayoutParams();
            layoutParams.height = val1.toString().toInt();
            ride_detail_view_holder.setLayoutParams(layoutParams);
            if (val1.toString().toInt() > screenHeight / 2) {
                floatBtnHolder.visibility = View.GONE
                driverStatusTopTextView.visibility = View.VISIBLE
                lineViewSlide.visibility = View.GONE
                HomeActivity.homeInstance.changeViewFocus(true)
            }
        });
        anim.setDuration(800);
        anim.start();


    }

    val screenHeight: Int get() = getScreenHight()

    private fun getScreenHight(): Int {

        return Resources.getSystem().displayMetrics.heightPixels
    }

    @Inject
    lateinit var presenter: HomePresenter

    override fun onActivityInject() {
        DaggerHomeComponent.builder().appComponent(getAppcomponent())
            .homeModule(HomeModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }


    @SuppressLint("MissingPermission")
    override fun onResponse(list: Any, int: Int) {
        Log.d("onResponse", "  " + Gson().toJson(list))

        when (int) {
            NetworkRequest.REQUEST_ACCEPT_BOOKING -> {
                var responseData = list as BaseResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {

                } else {
                    AppUtils.showSnackBar(activity!!, responseData.response_message)
                }
            }
            NetworkRequest.REQUEST_ORDER_DETAIL -> {


            }
        }
    }

    private fun updateMapView(
        responseObj: OrderDetailResponseModel.ResponseObj,
        isTrackForDelivery: Boolean
    ) {
//        HomeActivity.homeInstance.updateTrack(responseObj, isTrackForDelivery)
    }

    private fun getTourDetail() {
        val updateBookingRequestModel = OrderDetailRequestModel(orderID)
        presenter.getOrderDetail(
            NetworkRequest.REQUEST_ORDER_DETAIL,
            updateBookingRequestModel
        )
    }

    override fun showProgress() {
//        loaderView.show()
    }

    override fun hideProgress() {
//        loaderView.hide()
    }

    override fun noResult() {

    }

    override fun onError() {

    }

    override fun setPresenter(presenter: BasePresenter<*>) {

    }

    private var mCurrentLocation: LatLng? = null
    override fun whereIAM(loc: ADLocation?) {
        if (loc != null) {
            try {
                mCurrentLocation = LatLng(loc.lat, loc.longi)
                Prefs.putDouble(CommonValues.LATITUDE, loc.lat)
                Prefs.putDouble(CommonValues.LONGITUDE, loc.longi)
                Log.e(
                    TAG
                    , "whereIAM " + loc.address
                )

            } catch (e: Exception) {
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun requestLocationPermissions() {
        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
            goNext()
        } else {
            if (checkAndRequestLocationPermissions()) {
                //If you have already permitted the permission
                goNext()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun goNext() {
        MyTracker(activity, this).track()
    }
}
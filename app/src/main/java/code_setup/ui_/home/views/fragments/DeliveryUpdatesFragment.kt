package code_setup.ui_.home.views.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.base.mvp.BasePresenter
import com.electrovese.setup.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_tour_updates_layout.*
import kotlinx.android.synthetic.main.layout_delivery_updates_top_view.*
import kotlinx.android.synthetic.main.layout_new_request_view.*
import kotlinx.android.synthetic.main.layout_reach_for_pickup.*
import kotlinx.android.synthetic.main.layout_root_view.*
import kotlinx.android.synthetic.main.trans_toolbar_lay.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class DeliveryUpdatesFragment : CoreFragment(), HomeView, MyTracker.ADLocationListener {

    private var jobTimerCountdown: Long=60000// default value 60 sec
    lateinit var orderID: String
    var TAG: String = DeliveryUpdatesFragment::class.java.simpleName

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


    private fun setupCommonViewData(responseObj: OrderDetailResponseModel.ResponseObj) {
        orderID = responseObj!!.id
        orderTimeText.text = "Ordered at " + DateUtilizer.getFormatedDate(
            "dd-MM-yyyy, HH:mm",
            "hh:mm a",
            responseObj!!.booking_date
        )
        if (responseObj!!.products_count == 1)
            orderItemsText.setText("" + responseObj!!.products_count + " Item Total")
        else
            orderItemsText.setText("" + responseObj!!.products_count + " Items Total")

        orderNameText.setText(responseObj!!.user.name)
        orderIdText.setText(getString(R.string.str_order_id) + " " + responseObj!!.order_no)
        pickupLocationText.setText(responseObj.pickup_address.name)
        callCustomerBtn.setOnClickListener {
            //            AppUtils.showToast("working on this feature")
            if (!responseObj.user.contact.contains("+")) {
                InitCall("+" + responseObj.user.contact)
            } else
                InitCall(responseObj.user.contact)
        }
        callStoreBtn.setOnClickListener {
            //            AppUtils.showToast("working on this feature")
            if (!responseObj.pickup_address.contact.contains("+")) {
                InitCall("+" + responseObj.pickup_address.contact)
            } else
                InitCall(responseObj.pickup_address.contact)
        }
        navigationBtn.setOnClickListener {
            //            AppUtils.showToast("working on this feature")

            if (responseObj.status.equals(CommonValues.RIDER_STATUS_ASSIGNED)) {
                navigationIntent(
                    LatLng(responseObj.pickup_loc.lat, responseObj.pickup_loc.lng),
                    responseObj.pickup_address.name
                )
            } else if (responseObj.status.equals(CommonValues.RIDER_STATUS_OUT_FOR_DELIVERY) ||
                responseObj.status.equals(CommonValues.RIDER_STATUS_ON_DELIVERY_LOCATION)||
                responseObj.status.equals(CommonValues.RIDER_STATUS_ON_PICKUP_LOCATION)
            ) {
                navigationIntent(
                    LatLng(responseObj.delivery_loc.lat, responseObj.delivery_loc.lng),
                    responseObj.address.address
                )
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

    private fun setupOderDetails(dataObj: NotificationDataBodyModel?) {
        orderID = dataObj!!.id
        orderTimeText.text = "Ordered at " + DateUtilizer.getFormatedDate(
            "dd-MM-yyyy, HH:mm",
            "hh:mm a", dataObj!!.booking_date
        )
        if (dataObj!!.products_count == 1)
            orderItemsText.setText("" + dataObj!!.products_count + " Item Total")
        else
            orderItemsText.setText("" + dataObj!!.products_count + " Items Total")

        orderNameText.setText(dataObj!!.user.name)
        orderIdText.setText(getString(R.string.str_order_id) + " " + dataObj!!.order_no)
        startTimerWork()

        orderPickupLocationNameText.setText(dataObj!!.pickup_address.name)
        orderPickupLocationAddressText.setText(dataObj!!.pickup_address.address)

        orderDeliveryLocationNameText.setText(AppUtils.capWorldString(dataObj!!.delivery_address.type))
        orderDeliveryLocationAddressText.setText(dataObj!!.delivery_address.address)

        rejectDelivery.setSafeOnClickListener {
            mCurrentLocation = AppUtils.getMyLocation()
            AppUtils.clearNotification(activity!!, CommonValues.NEW_JOB_MOTIFICATION_ID)

            AppDialogs.openDialogTwoButton(activity!!,
                getString(R.string.strRejecting_job)
                ,
                getString(R.string.strRejecting_job_message),
                object : OnBottomDialogItemListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any) {
                        when (type) {
                            CommonValues.REJECT -> {
                                presenter.updateBookingRequest(
                                    NetworkRequest.REQUEST_REJECT_BOOKING,
                                    OrderDetailRequestModel(
                                        orderID, mCurrentLocation!!.latitude,
                                        mCurrentLocation!!.longitude
                                    )
                                )
                                HomeActivity.homeInstance.backToHomeView()
                            }
                        }
                    }
                })


        }
        acceptDelivery.setSafeOnClickListener {
            mCurrentLocation = AppUtils.getMyLocation()
            AppUtils.clearNotification(activity!!, CommonValues.NEW_JOB_MOTIFICATION_ID)
            presenter.updateBookingRequest(
                NetworkRequest.REQUEST_ACCEPT_BOOKING,
                OrderDetailRequestModel(
                    orderID, mCurrentLocation!!.latitude,
                    mCurrentLocation!!.longitude
                )
            )
        }
    }

    private fun startTimerWork() {
        object : CountDownTimer(jobTimerCountdown/*dataBody.expiration_date*/, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
                try {
                    timerText.setText("" + seconds)
                } catch (e: Exception) {
                }
            }

            override fun onFinish() {
                try {
                    timerText.setText("00")
                    HomeActivity.homeInstance.backToHomeView()
                } catch (e: Exception) {
                }
            }
        }.start()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var vw: View = inflater.inflate(R.layout.fragment_tour_updates_layout, container, false)
        // Inflate the layout for this fragment
//        changeGravityToBottom(vw)
        return vw
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestLocationPermissions()
        timerViewRl.bringToFront()
        try {
            HomeActivity.homeInstance.txtToolbartitle.setText(R.string.str_delivery_status)
        } catch (e: Exception) {
        }

        statusUpdateButton.setSafeOnClickListener {
            mCurrentLocation = AppUtils.getMyLocation()

            if (statusUpdateButton.text.toString().equals(getString(R.string.str_reached_pickup_point))) {
                presenter.reachOnPickupRequest(
                    NetworkRequest.REQUEST_ON_PICKUP_LOCATION,
                    UpdateOrderRequestModel(
                        mCurrentLocation!!.latitude,
                        mCurrentLocation!!.longitude,
                        orderID
                    )
                )
            } else if (statusUpdateButton.text.toString().equals(getString(R.string.str_out_for_delivery))) {
                presenter.requestOutForAndOnTheway(
                    NetworkRequest.REQUEST_ON_THE_WAY,
                    UpdateOrderRequestModel(
                        mCurrentLocation!!.latitude,
                        mCurrentLocation!!.longitude,
                        orderID
                    )
                )
            } else if (statusUpdateButton.text.toString().equals(getString(R.string.str_reached_drop_off))) {
                presenter.reachOnDropRequest(
                    NetworkRequest.REQUEST_ON_DROP_OFF_LOCATION,
                    UpdateOrderRequestModel(
                        mCurrentLocation!!.latitude,
                        mCurrentLocation!!.longitude,
                        orderID
                    )
                )
            } else if (statusUpdateButton.text.toString().equals(getString(R.string.str_order_delivered))) {
                presenter.requestOrderDelivered(
                    NetworkRequest.REQUEST_ORDER_DELIVERED,
                    UpdateOrderRequestModel(
                        mCurrentLocation!!.latitude,
                        mCurrentLocation!!.longitude,
                        orderID
                    )
                )
            }

        }


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
                    Prefs.putString(CommonValues.CURRENT_ORDER_ID, orderID)
                    registerLocationUpdateEvents()
                    getOrderDetail()
                } else {
                    AppUtils.showSnackBar(activity!!, responseData.response_message)
                }
            }
            NetworkRequest.REQUEST_ORDER_DETAIL -> {
                var rModel = list as OrderDetailResponseModel
                loaderView.hide()

            }
        }
    }

    private fun updateMapView(
        responseObj: OrderDetailResponseModel.ResponseObj,
        isTrackForDelivery: Boolean
    ) {
//        HomeActivity.homeInstance.updateTrack(responseObj, isTrackForDelivery)
    }

    private fun getOrderDetail() {
        val updateBookingRequestModel = OrderDetailRequestModel(orderID)
        presenter.getOrderDetail(
            NetworkRequest.REQUEST_ORDER_DETAIL,
            updateBookingRequestModel
        )
    }

    override fun showProgress() {
        loaderView.show()
    }

    override fun hideProgress() {
        loaderView.hide()
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
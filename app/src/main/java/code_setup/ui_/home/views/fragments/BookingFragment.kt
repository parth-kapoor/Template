package code_setup.ui_.home.views.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import code_setup.app_core.BaseApplication
import code_setup.app_core.CoreFragment
import code_setup.app_models.other_.ADLocation
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_models.request_.GetPriceListRequestModel
import code_setup.app_models.request_.OrderDetailRequestModel
import code_setup.app_models.response_.BaseResponseModel
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.*
import code_setup.app_util.callback_iface.OnBottomDialogItemListener
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.app_util.location_utils.MyTracker
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.home.apapter_.CabsListAdapter
import code_setup.ui_.home.di_home.DaggerHomeComponent
import code_setup.ui_.home.di_home.HomeModule
import code_setup.ui_.home.home_mvp.HomePresenter
import code_setup.ui_.home.home_mvp.HomeView
import code_setup.ui_.home.models.BookCabRequestModel
import code_setup.ui_.home.models.PriceListResponseModel
import code_setup.ui_.home.views.HomeActivity
import code_setup.ui_.home.views.ride.RideSchedulingActivity
import code_setup.ui_.settings.views.payments.SelectPaymentTypeActivity
import com.base.mvp.BasePresenter
import com.electrovese.setup.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.android.synthetic.main.cabs_detail_view.*
import kotlinx.android.synthetic.main.fragment_booking_layout.*
import kotlinx.android.synthetic.main.fragment_tour_updates_layout.loaderView
import kotlinx.android.synthetic.main.layout_delivery_updates_top_view.*
import kotlinx.android.synthetic.main.layout_request_cabs_view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject


class BookingFragment : CoreFragment(), HomeView, MyTracker.ADLocationListener {

    lateinit var cabsAdapter: CabsListAdapter
    private var jobTimerCountdown: Long = 60000// default value 60 sec
    lateinit var orderID: String
    var TAG: String = BookingFragment::class.java.simpleName

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
            EVENTS.NEW_TAXI_REQUEST_CONFIRM_BOOKING -> {
                getPriceList()

            }
        }
    }

    private fun getPriceList() {
        cabsRecyclar.showShimmerAdapter()
        presenter.getPriceList(
            NetworkRequest.REQUEST_GET_PRICE_LIST,
            GetPriceListRequestModel(getEndLocation(), getStartLocation())
        )
    }

    private fun getStartLocation(): List<Double> {
        var startCordinatesArray = ArrayList<Double>()
        startCordinatesArray.add(BaseApplication.selectedMyLocation!!.lat)
        startCordinatesArray.add(BaseApplication.selectedMyLocation!!.lng)
        return startCordinatesArray
    }

    private fun getEndLocation(): List<Double> {
        var startCordinatesArray = ArrayList<Double>()
        startCordinatesArray.add(BaseApplication.selectedDestinationLocation!!.lat)
        startCordinatesArray.add(BaseApplication.selectedDestinationLocation!!.lng)
        return startCordinatesArray
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
        var vw: View = inflater.inflate(R.layout.fragment_booking_layout, container, false)
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
//          HomeActivity.homeInstance.txtToolbartitle.setText(R.string.str_delivery_status)
            HomeActivity.homeInstance.showBackArrow()
        } catch (e: Exception) {
        }

        showCabSelectionView()
        showRoutOnMap()
        showScheduleView()
    }

    private fun showRoutOnMap() {
        HomeActivity.homeInstance.showtourRoute(
            LatLng(
                BaseApplication.selectedMyLocation!!.lat,
                BaseApplication.selectedMyLocation!!.lng
            ),
            LatLng(
                BaseApplication.selectedDestinationLocation!!.lat,
                BaseApplication.selectedDestinationLocation!!.lng
            )
        )
    }

    private fun showCabSelectionView() {
        topArrowView.visibility = View.VISIBLE
        new_booking_view_holder.visibility = View.VISIBLE
        initCabsAdapter()
        val function: (View) -> Unit = {
            val intent = android.content.Intent(
                activity!!,
                SelectPaymentTypeActivity::class.java
            )
//            intent.putExtra(CommonValues.Project_ID, t as String)
//                        intent.putExtra(
//                            CommonValues.Project_DATA,
//                            featuredAdapter.getselctedPosition(position)
//                        )
            val options =
                androidx.core.app.ActivityOptionsCompat.makeSceneTransitionAnimation(
                    activity!!,
                    paymentTypeLayout!!, paymentTypeLayout.transitionName
                )
            startActivityForResult(
                intent,
                CommonValues.REQUEST_CODE_LOCATION_PICKER,
                options.toBundle()
            )
        }
        changepaymentTypeText.setOnClickListener(function)
        paymentTypeText.setOnClickListener(function)
        confirmBtn.setOnClickListener {
            if (confirmBtn.text.toString().toLowerCase().equals(getString(R.string.str_confirm_booking).toLowerCase())) {
                if (cabsAdapter.getSelectedCabData().isNotEmpty()) {
                    presenter.bookRideRequest(
                        NetworkRequest.REQUEST_BOOK_RIDE,
                        getBookingRequestData()
                    )
                } else {
                    AppUtils.showSnackBar(activity!!, getString(R.string.str_select_cab))
                }
            } else if (confirmBtn.text.toString().toLowerCase().equals(getString(R.string.strScheduleNow).toLowerCase())) {
                activitySwitcher(activity!!, RideSchedulingActivity::class.java, null)
            }
        }

        timedImgBtn.setOnClickListener {
            AppDialogs.openDialogScheduleBooking(
                activity!!,
                Any(),
                Any(),
                object : OnBottomDialogItemListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any) {
                        showScheduleView()
                    }
                })
        }
    }

    private fun getBookingRequestData(): BookCabRequestModel {
        return BookCabRequestModel(
            BaseApplication.selectedDestinationLocation!!.addresName.toString(),
            getEndLocation(), BaseApplication.selectedMyLocation!!.addresName.toString(),
            getStartLocation(), cabsAdapter.getSelectedCabData()
        )

    }

    private fun showScheduleView() {
        if (BaseApplication.scheduledDate.isNullOrEmpty()) {
            timedImgBtn.visibility = View.VISIBLE
            confirmBtn.setText(R.string.str_confirm_booking)
            scheduledDateTimeTxtbooking.setText(BaseApplication.scheduledDate)
            scheduleViewHolder.visibility = View.GONE
            chooseTopTxt.visibility = View.VISIBLE
        } else {
            timedImgBtn.visibility = View.GONE
            confirmBtn.setText(R.string.strScheduleNow)
            scheduledDateTimeTxtbooking.setText(BaseApplication.scheduledDate)
            scheduleViewHolder.visibility = View.VISIBLE
            chooseTopTxt.visibility = View.GONE
        }
    }

    private fun initCabsAdapter() {
        cabsRecyclar.showShimmerAdapter()
        Handler().postDelayed(Runnable {
            cabsRecyclar.hideShimmerAdapter()
        }, 1000)

        with(cabsRecyclar) {
            cabsAdapter = CabsListAdapter(
                activity!!,
                ArrayList(),
                object : OnItemClickListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any?) {
                        showDetailData(cabsAdapter.getPositionData(position))
                    }
                })
            layoutManager = LinearLayoutManager(activity, VERTICAL, false)
            adapter = cabsAdapter
        }


    }

    private fun showDetailData(positionData: PriceListResponseModel.ResponseObj) {
        cabFareEstimationHolder.visibility = View.VISIBLE
        cabsRecyclar.visibility = View.GONE
        cabFareEstimationHolder.setOnClickListener {
            cabsRecyclar.visibility = View.VISIBLE
            cabFareEstimationHolder.visibility = View.GONE
        }
        detailCabType.setText(positionData.name)
        detailTotalPrice.setText(getString(R.string.str_rupeee_symbol) + "" + positionData.total_price)
        carSeatsTxt.setText(positionData.capacity + " Seats")
        totalsDistacneTxt.setText(positionData.price.total_distance + " KMs")
        carDistacneTimeTxt.setText(positionData.est_time + " away")

        baseFairTxt.setText(getString(R.string.str_rupeee_symbol) + "" + positionData.price.base_price)
        perKmFairTxt.setText(getString(R.string.str_rupeee_symbol) + "" + positionData.price.per_km_price)
        convninceFeeTxt.setText(getString(R.string.str_rupeee_symbol) + "" + positionData.price.convenience_charges)
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
            NetworkRequest.REQUEST_GET_PRICE_LIST -> {
                var responseData = list as PriceListResponseModel
//                cabsRecyclar.hideShimmerAdapter()
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    if (responseData.response_obj != null && responseData.response_obj.isNotEmpty()) {
                        cabsAdapter.updateAll(responseData.response_obj)
                    }

                } else {
                    AppUtils.showSnackBar(activity!!, responseData.response_message)
                }
            }
            NetworkRequest.REQUEST_BOOK_RIDE -> {
                var rModel = list as BaseResponseModel
                if (rModel.response_code == NetworkCodes.SUCCEES.nCodes) {
                    HomeActivity.homeInstance.viewModes(
                        VIEW_MODE.LOADING.nCodes,
                        TripUpdatesFragment()
                    )
                } else {
                    AppUtils.showSnackBar(activity!!, rModel.response_message)
                }

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
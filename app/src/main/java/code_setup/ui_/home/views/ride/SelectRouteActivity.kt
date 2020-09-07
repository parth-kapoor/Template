package code_setup.ui_.home.views.ride

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import code_setup.app_core.BaseApplication
import code_setup.app_core.CoreActivity
import code_setup.app_util.AnimUtils
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.ui_.home.apapter_.GooglePlaceAdapter
import code_setup.ui_.home.models.GooglePlaceDetailResponseModel
import code_setup.ui_.home.models.GooglePlaceResponseModel
import code_setup.ui_.home.models.other.SelectedLocationModel
import com.electrovese.setup.R
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.address_selection_view.*
import kotlinx.android.synthetic.main.common_toolbar_lay.*
import kotlinx.android.synthetic.main.common_toolbar_with_appbar.*
import kotlinx.android.synthetic.main.layout_schedule_date_time_view.*
import kotlinx.android.synthetic.main.select_route_activity.*
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList

class SelectRouteActivity : CoreActivity() {
    override fun onActivityInject() {
    }

    override fun getScreenUi(): Int {
        return R.layout.select_route_activity
    }

    private var locationSelctetd: Boolean = false
    var isForDestination: Boolean = false
    private lateinit var googlePlaceAdapter: GooglePlaceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backToolbar, false)
        AnimUtils.moveAnimationX(backToolbar, true)
        txtTitletoolbar.setText(R.string.taxi_booking)
        val function: (View) -> Unit = {
            AppUtils.hideKeyboard(backToolbar)
            onBackPressed()
        }
        backToolbar.setOnClickListener(function)
        initAdapter()
        initSearchView()
        getIntenData()
    }

    private fun getIntenData() {
        if (!intent.getStringExtra(CommonValues.SCHEDULED_DATE).isNullOrEmpty()) {
            scheduledDateTxtHolder.visibility = View.VISIBLE
            scheduledDateTimeTxt.text = intent.getStringExtra(CommonValues.SCHEDULED_DATE)
        }
        try {
            if (!Prefs.getString(CommonValues.CURRENT_ADDRESS, "").isNullOrEmpty()) {
                locationSelctetd = true
                myLocationView.setQuery(Prefs.getString(CommonValues.CURRENT_ADDRESS, ""), false)
                destinationLocationView.requestFocus()
                BaseApplication.selectedMyLocation = getAutoSelectionObject()
                BaseApplication.selectedDestinationLocation=null
            }
        } catch (e: Exception) {
        }
    }

    private fun getAutoSelectionObject(): SelectedLocationModel? {
        Prefs.getString(CommonValues.CURRENT_ADDRESS, "")
        var selectedLocation = SelectedLocationModel()
        selectedLocation.addresName =
            getformatedAddress(Prefs.getString(CommonValues.CURRENT_ADDRESS, "")!!)
        selectedLocation.lat = Prefs.getDouble(CommonValues.LATITUDE, 0.0)
        selectedLocation.lng = Prefs.getDouble(CommonValues.LONGITUDE, 0.0)
        selectedLocation.addressID = "result.place_id"
        return selectedLocation
    }

    private fun initSearchView() {
        myLocationView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // do something on text submit
                Log.d("onQueryTextSubmit", "  " + query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // do something when text changes
                Log.d("onQueryTextChange", "  " + newText)
                if (newText.isNotBlank() && newText.length > 2 && !locationSelctetd) {
                    isForDestination = false
                    requestSearchQuery(newText)
                } else {
                    otherAddressesHolder.visibility = View.VISIBLE
                    val gone = View.GONE
                    addressSelectionRecyclar.visibility = gone
                    noLoactionView.visibility = gone
                    locationSelctetd = false
                }
                return true
            }
        })


        destinationLocationView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // do something on text submit
                Log.d("onQueryTextSubmit", "  " + query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // do something when text changes
                Log.d("onQueryTextChange", "  " + newText)
                if (newText.isNotBlank() && newText.length > 2 && !locationSelctetd) {
                    isForDestination = true
                    requestSearchQuery(newText)
                } else {
                    otherAddressesHolder.visibility = View.VISIBLE
                    val gone = View.GONE
                    addressSelectionRecyclar.visibility = gone
                    noLoactionView.visibility = gone
                    locationSelctetd = false
                }
                return true
            }
        })
    }

    private fun requestSearchQuery(s: String) {
        var mCompositeDisposable: CompositeDisposable? = null
//            searchLoader.show()
        mCompositeDisposable = CompositeDisposable()
        val apiService = RestConfig.createGoogleApi()
        val getplaceAutocomplete = apiService.getplaceAutocompleteEmbbed(
            Prefs.getString(CommonValues.ACCESS_TOKEN, UUID.randomUUID().toString()).toString()
            /*UUID.randomUUID().toString()*/,
            s.toString(),
            AppUtils.getMyLocation()!!.latitude.toString() + "," + AppUtils.getMyLocation()!!.longitude.toString(),
            "5000",
            "ADD YOUR KEY HERE"
        )
        mCompositeDisposable?.add(
            getplaceAutocomplete
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSuccess, this::handleFaliur)
        )

    }

    fun handleSuccess(baseResponse: GooglePlaceResponseModel) {
        Log.d("  success ", "  " + Gson().toJson(baseResponse))
        if (baseResponse.predictions != null && baseResponse.predictions.size > 0) {
            googlePlaceAdapter.updateAll(getFilteredPredictions(baseResponse.predictions))
            googlePlaceAdapter.setSearchBoolean(isForDestination)
            addressSelectionRecyclar.adapter = googlePlaceAdapter
            googlePlaceAdapter.notifyDataSetChanged()
            showSearchViewWithResult()
        } else {
            Log.d("success ", " No Result  " + Gson().toJson(baseResponse))
            showOtherViews()
        }
        if (googlePlaceAdapter.itemCount == 0) {
            AppUtils.showSnackBar(this, getString(R.string.str_no_address_found))
        }
    }

    private fun showOtherViews() {
        addressSelectionRecyclar.visibility = View.GONE
        noLoactionView.visibility = View.VISIBLE
//        otherAddressesHolder.visibility = View.VISIBLE
    }

    private fun showSearchViewWithResult() {
        addressSelectionRecyclar.visibility = View.VISIBLE
        noLoactionView.visibility = View.GONE
        otherAddressesHolder.visibility = View.GONE
    }

    private fun getFilteredPredictions(predictions: List<GooglePlaceResponseModel.Prediction>): List<GooglePlaceResponseModel.Prediction> {
        var list = ArrayList<GooglePlaceResponseModel.Prediction>()

        for (i in 0 until predictions.size) {
            list.add(predictions.get(i))

        }
        return list
    }

    fun handleFaliur(error: Throwable) {
        Log.d("Faliur ", "   ")
    }

    private fun initAdapter() {
        with(addressSelectionRecyclar) {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(
                this@SelectRouteActivity,
                androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
                false
            )
            googlePlaceAdapter =
                code_setup.ui_.home.apapter_.GooglePlaceAdapter(this@SelectRouteActivity,
                    ArrayList(), object : OnItemClickListener<Any> {
                        override fun onItemClick(view: View, position: Int, type: Int, t: Any?) {
                            locationSelctetd = true
                            if (isForDestination) {
                                destinationLocationView.setQuery(
                                    (t as GooglePlaceResponseModel.Prediction).description,
                                    false
                                )
                            } else {
                                myLocationView.setQuery(
                                    (t as GooglePlaceResponseModel.Prediction).description,
                                    false
                                )
                            }

                            addressSelectionRecyclar.visibility = View.GONE
                            otherAddressesHolder.visibility = View.VISIBLE
                            getPlaceDetails((t as GooglePlaceResponseModel.Prediction).place_id)

                        }
                    })
            adapter = googlePlaceAdapter
        }
    }

    private fun getPlaceDetails(placeId: String) {
        //        searchLoader.show()
        var mCompositeDisposable = CompositeDisposable()
        val apiService = RestConfig.createGoogleApi()
        mCompositeDisposable?.add(
            apiService.getplaceDetail(
                Prefs.getString(CommonValues.ACCESS_TOKEN, UUID.randomUUID().toString()).toString(),
                placeId,
                "ADD YOUR KEY HERE"
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSuccessDetail, this::handleFaliurDetail)
        )

    }

    fun handleFaliurDetail(error: Throwable) {

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun handleSuccessDetail(baseResponse: GooglePlaceDetailResponseModel) {
        Log.d("  success ", "  " + Gson().toJson(baseResponse))
        try {
            if (isForDestination) {
                BaseApplication.selectedDestinationLocation =
                    getSelectionObject(baseResponse.result)
            } else
                BaseApplication.selectedMyLocation = getSelectionObject(baseResponse.result)

            if (BaseApplication.selectedDestinationLocation != null && BaseApplication.selectedMyLocation != null) {
                AppUtils.hideKeyboard(backToolbar)
                onBackPressed()
            }


        } catch (e: Exception) {
            AppUtils.showSnackBar(this, getString(R.string.str_location_error))
        }

    }

    private fun getSelectionObject(result: GooglePlaceDetailResponseModel.Result): SelectedLocationModel? {
        var selectedLocation = SelectedLocationModel()
        selectedLocation.addresName = getformatedAddress(result.adr_address)
        selectedLocation.lat = result.geometry.location.lat
        selectedLocation.lng = result.geometry.location.lng
        selectedLocation.addressID = result.place_id
        return selectedLocation
    }

    private fun getformatedAddress(adrAddress: String): String {
        var address = StringBuilder()
        for (i in 0 until adrAddress.split(",").size) {
            if (i > 0)
                address.append(adrAddress.split(",")[i]).append(",")
        }
        return address.toString()
    }

    override fun onBackPressed() {
        setResult(CommonValues.REQUEST_CODE_LOCATION_PICKER)
        super.onBackPressed()
    }

    override fun onError() {

    }

}
package code_setup.ui_.home.home_mvp

import android.app.Activity
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.util.Log
import code_setup.app_core.BaseApplication
import code_setup.app_models.other_.ADLocation
import code_setup.app_models.other_.CabsDataModel
import code_setup.app_models.other_.NotificationDataBodyModel
import code_setup.app_models.request_.*
import code_setup.app_models.response_.CaptureInfoModel
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.DateUtilizer
import code_setup.app_util.direction_utils.Distance
import code_setup.app_util.direction_utils.Duration
import code_setup.app_util.direction_utils.GeocodedResponse
import code_setup.app_util.direction_utils.Route
import code_setup.app_util.location_utils.MyTracker
import code_setup.app_util.location_utils.log
import code_setup.net_.NetworkRequest
import code_setup.ui_.home.models.BookCabRequestModel
import code_setup.ui_.home.views.HomeActivity
import com.base.mvp.BasePresenter
import com.base.util.SchedulerProvider
import com.electrovese.kotlindemo.networking.ApiInterface
import com.electrovese.setup.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import io.reactivex.disposables.CompositeDisposable
import java.io.IOException
import java.util.*
import javax.inject.Inject


class HomePresenter @Inject constructor(
    var api: ApiInterface,
    disposable: CompositeDisposable,
    scheduler: SchedulerProvider
) : BasePresenter<HomeView>(disposable, scheduler) {


    private val TAG: String = HomePresenter::class.java.simpleName


    /**
     * Add custom to map view
     */
    fun customMap(context: Activity, googleMap: GoogleMap) {
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            var success: Boolean = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    context, R.raw.style_json1
                )
            )

            if (!success) {
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Can't find style. Error: ", e)
        }
    }


    fun changeDriverStatus(
        requestChangeStatus: Int
    ) {
        view?.showProgress()
        disposable.add(
            api.changeDriverStatus(
                BaseApplication.instance.getCommonHeaders()
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, requestChangeStatus)

                    },
                    { _ ->
                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }


    fun captureInfo(requestCode: Int, captureInfoData: CaptureInfoModel) {
//        view?.showProgress()
        disposable.add(
            api.captureInfo(BaseApplication.instance.getCommonHeaders(), captureInfoData)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                        view?.hideProgress()
                        view?.onResponse(result, requestCode)

                    },
                    { _ ->
                        //                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }


    fun updateBookingRequest(
        reqCode: Int,
        updateBookingRequestModel1: OrderDetailRequestModel
    ) {

        view?.showProgress()
        when (reqCode) {
            NetworkRequest.REQUEST_ACCEPT_BOOKING -> {
                disposable.add(
                    api.acceptTourRequest(
                        BaseApplication.instance.getCommonHeaders(),
                        updateBookingRequestModel1
                    )
                        .subscribeOn(scheduler.io())
                        .observeOn(scheduler.ui())
                        .subscribe(
                            { result ->
                                view?.hideProgress()
                                view?.onResponse(result, reqCode)

                            },
                            { _ ->
                                view?.hideProgress()
                                view?.onError()
                            })
                )
            }

            NetworkRequest.REQUEST_REJECT_BOOKING -> {
                disposable.add(
                    api.rejectTourRequest(
                        BaseApplication.instance.getCommonHeaders(),
                        updateBookingRequestModel1
                    )
                        .subscribeOn(scheduler.io())
                        .observeOn(scheduler.ui())
                        .subscribe(
                            { result ->
                                //                                view?.hideProgress()
                                view?.onResponse(result, reqCode)

                            },
                            { _ ->
                                //                                view?.hideProgress()
                                view?.onError()
                            })
                )
            }

        }
    }

    fun getCurrentStatus(requestCode: Int) {
        view?.showProgress()
        disposable.add(
            api.getStatusRequest(BaseApplication.instance.getCommonHeaders())
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, requestCode)

                    },

                    {
                        log("getCurrentStatus " + it.localizedMessage)

                        view?.hideProgress()
                        view?.onError()
                        /*    try {
                                if (it is HttpException) {
                                    val exception: HttpException = it as HttpException
                                    when (exception.code()) {
                                        400 -> {
                                            log("getCurrentStatus   Error :  400" )
                                        }
                                        500 -> {
                                            log("getCurrentStatus   Error :  500" )
                                        }
                                        else -> {
                                            log("getCurrentStatus    Error :  else")
                                        }
                                    }
                                }
                            } catch (e: Exception) {
                                log("Exception     :  "+e.localizedMessage)
                            }*/


                    })
        )
    }


    fun getOrderDetail(requestCode: Int, updateBookingRequestModel: OrderDetailRequestModel) {
        view?.showProgress()
        disposable.add(
            api.getOrderDetail(
                BaseApplication.instance.getCommonHeaders(),
                updateBookingRequestModel
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }


    fun logoutUser(requestCode: Int) {
//        view?.showProgress()
        disposable.add(
            api.logoutUser(
                BaseApplication.instance.getCommonHeaders()
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                        view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        //                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    fun reachOnPickupRequest(requestCode: Int, updateOrderRequestModel: UpdateOrderRequestModel) {
//        view?.showProgress()
        disposable.add(
            api.requestUpdateDriverStatus(
                BaseApplication.instance.getCommonHeaders(),
                updateOrderRequestModel
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                                    view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        //                                    view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    fun requestOutForAndOnTheway(
        requestCode: Int,
        updateOrderRequestModel: UpdateOrderRequestModel
    ) {
//        view?.showProgress()
        disposable.add(
            api.requestOutForandOnWay(
                BaseApplication.instance.getCommonHeaders(),
                updateOrderRequestModel
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                                    view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        //                                    view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    fun reachOnDropRequest(requestCode: Int, updateOrderRequestModel: UpdateOrderRequestModel) {
//        view?.showProgress()
        disposable.add(
            api.requestReachedDropLocation(
                BaseApplication.instance.getCommonHeaders(),
                updateOrderRequestModel
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    fun requestOrderDelivered(requestCode: Int, updateOrderRequestModel: UpdateOrderRequestModel) {
//        view?.showProgress()
        disposable.add(
            api.requestOrderDelivered(
                BaseApplication.instance.getCommonHeaders(),
                updateOrderRequestModel
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                                    view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        //                                    view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    fun getModifiedList(arrayList: GeocodedResponse): ArrayList<Route> {
        val routes = ArrayList<Route>()
        arrayList.routes.get(0).overviewPolyline
        for (i in 0 until arrayList.routes.size) {

            val jsonRoute = arrayList.routes.get(i)
            val route = Route()

            val overview_polylineJson = jsonRoute.overviewPolyline
            val jsonLegs = jsonRoute.legs
            val jsonLeg = jsonLegs.get(0)
            val jsonDistance = jsonLeg.distance
            val jsonDuration = jsonLeg.duration
            val jsonEndLocation = jsonLeg.start_location
            val jsonStartLocation = jsonLeg.end_location

            route.distance = Distance(jsonDistance.text, jsonDistance.value)
            route.duration = Duration(jsonDuration.text, jsonDuration.value)
            route.endAddress = jsonLeg.end_address
            route.startAddress = jsonLeg.start_address
            route.startLocation = LatLng(jsonStartLocation.lat, jsonStartLocation.lng)
            route.endLocation = LatLng(jsonEndLocation.lat, jsonEndLocation.lat)
            route.points = decodePolyLine(overview_polylineJson.points)

            routes.add(route)
        }
        return routes
    }

    private fun decodePolyLine(poly: String): List<LatLng> {
        val len = poly.length
        var index = 0
        val decoded = ArrayList<LatLng>()
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = poly[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            decoded.add(
                LatLng(
                    lat / 100000.0, lng / 100000.0
                )
            )
        }
        return decoded
    }

    fun getDestinationMarker(
        mMap: GoogleMap,
        storeLocation: LatLng,
        destStr:String

    ): Marker? {
        return mMap.addMarker(
            MarkerOptions().position(storeLocation!!).flat(true)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_destination)).anchor(
                    0.5f,
                    0.5f
                ).title(destStr)
        )
    }

    fun getMyMarker(
        mMap: GoogleMap,
        storeLocation: LatLng,
        labetStr: String
    ): Marker? {
        var title = "Delivery Location"
        if (labetStr.isNullOrEmpty()) {
            title = "My location"
        } else {
            title = "My location"
        }

        return mMap.addMarker(
            MarkerOptions().position(storeLocation!!).flat(true)
                .icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_my_location)
                ).anchor(
                    0.5f,
                    0.5f
                ).title(title)
        )
    }


    fun getMyLocationMarker(mMap: GoogleMap, mDefaultLocation: LatLng?): Marker? {
        return mMap.addMarker(
            MarkerOptions().position(mDefaultLocation!!).flat(true)
                .icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_deliver_boy)
                ).anchor(
                    0.5f,
                    0.5f
                ).title("My Location")
        )
    }

    fun getWishTextMessage(): String? {
        var message = "Good Afternoon"
        val time = DateUtilizer.getCurrentDate("HH:mm").split(":")[0].toInt()
        Log.e("getWishTextMessage"," "+time)
        if (time < 12) {
            message = "Good Morning"
        } else if (time in 13..17
        ) {
            message = "Good Afternoon"
        } else if (time > 18) {
            message = "Good Evening"
        }

        return message
    }

    fun fetchLocation(
        ctx: HomeActivity,
        lat: Double?,
        lng: Double?,
        param: MyTracker.ADLocationListener
    ) {
        val addresses: List<Address>
        val geocoder = Geocoder(ctx, Locale.getDefault())
        try {
            val adLocation = ADLocation()
            try {
                addresses =
                    geocoder.getFromLocation(
                        lat!!,
                        lng!!,
                        1
                    )
                adLocation.address = addresses[0]
                    .getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            } catch (e: IOException) {
                return
            }
            adLocation.city = addresses[0].locality
            adLocation.state = addresses[0].adminArea
            adLocation.country = addresses[0].countryName
            adLocation.pincode = addresses[0].postalCode
            adLocation.lat = lat!!
            adLocation.longi = lng!!
            param.whereIAM(adLocation)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getPriceList(requestCode: Int, getPriceListRequestModel: GetPriceListRequestModel) {
//        view?.showProgress()
        disposable.add(
            api.requestPriceList(
                BaseApplication.instance.getCommonHeaders(),
                getPriceListRequestModel
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                                    view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        //                                    view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    fun bookRideRequest(requestCode: Int, bookingRequestData: BookCabRequestModel) {
        disposable.add(
            api.requestBookCab(
                BaseApplication.instance.getCommonHeaders(),
                bookingRequestData
            )
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        //                                    view?.hideProgress()
                        view?.onResponse(result, requestCode)
                    },
                    { _ ->
                        //                                    view?.hideProgress()
                        view?.onError()
                    })
        )
    }
}
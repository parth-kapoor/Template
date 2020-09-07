package code_setup.ui_.settings.settings_mvp

import android.app.Activity
import android.content.res.Resources
import android.util.Log
import code_setup.app_core.BaseApplication
import code_setup.app_models.request_.BookingHistoryRequest
import code_setup.app_models.request_.RequestSupportModel
import code_setup.app_models.request_.SubmitReviewRequest
import com.base.mvp.BasePresenter
import com.base.util.SchedulerProvider
import com.electrovese.kotlindemo.networking.ApiInterface
import com.electrovese.setup.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MapStyleOptions
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class SettingsPresenter @Inject constructor(
        var api: ApiInterface,
        disposable: CompositeDisposable,
        scheduler: SchedulerProvider
) : BasePresenter<SettingsView>(disposable, scheduler) {


    private val TAG: String = SettingsPresenter::class.java.simpleName

    fun getRepos(searchKey: String) {

        view?.showProgress()
        disposable.add(
                api.search(searchKey)
                        .subscribeOn(scheduler.io())
                        .observeOn(scheduler.ui())
                        .subscribe(
                                { result ->
                                    view?.hideProgress()
                                    view?.onResponse(result, 1)

                                },
                                { _ ->
                                    view?.hideProgress()
                                    view?.onError()
                                })
        )
    }


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

    fun getProfileData(reqCode: Int) {
        view?.showProgress()
        disposable.add(
                api.getProfileDetail(BaseApplication.instance.getCommonHeaders())
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

    fun getRideHistory(reqCode: Int, bookingHistoryRequest: BookingHistoryRequest) {
       /* view?.showProgress()
        disposable.add(
                api.getBookingHistory(BaseApplication.instance.getCommonHeaders(), bookingHistoryRequest)
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
        )*/
    }


    fun updtaeProfileImage(reqCode: Int, requiredData: MultipartBody.Part) {
        view?.showProgress()
        disposable.add(
                api.updateProfileImage(BaseApplication.instance.getCommonHeaders(), requiredData)
                        .subscribeOn(scheduler.io())
                        .observeOn(scheduler.ui())
                        .subscribe(
                                { result ->
                                   view?.hideProgress()
                                    view?.onResponse(result, reqCode)
                                },
                                {
                                    Log.e("","updtaeProfileImage " + it.localizedMessage)
                                    view?.hideProgress()
                                    view?.onError()
                                })
        )
    }

    fun submitSupportRequest(reqCode: Int, requiredData: RequestSupportModel) {
        view?.showProgress()
        disposable.add(
            api.requestSupport(BaseApplication.instance.getCommonHeaders(), requiredData)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, reqCode)
                    },
                    {
                        Log.e("","updtaeProfileImage " + it.localizedMessage)
                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }
}
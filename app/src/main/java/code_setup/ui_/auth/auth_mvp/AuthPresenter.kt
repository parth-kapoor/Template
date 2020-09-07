package code_setup.ui_.auth.auth_mvp

import code_setup.app_core.BaseApplication
import com.base.mvp.BasePresenter
import com.base.util.SchedulerProvider
import code_setup.app_models.request_.LoginRequestModel
import code_setup.app_util.location_utils.log
import code_setup.ui_.auth.models.request_model.RequestOTPModel
import code_setup.ui_.auth.models.request_model.RequestVerifyOtp
import com.burakeregar.githubsearch.home.presenter.AuthView
import com.electrovese.kotlindemo.networking.ApiInterface
import io.reactivex.disposables.CompositeDisposable
import okhttp3.RequestBody
import javax.inject.Inject

class AuthPresenter @Inject constructor(
    var api: ApiInterface,
    disposable: CompositeDisposable,
    scheduler: SchedulerProvider
) : BasePresenter<AuthView>(disposable, scheduler) {

    fun requestOtp(reqCode: Int, requestOTPModel: RequestOTPModel) {
        view?.showProgress()
        disposable.add(
            api.requestOTP(requestOTPModel)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, reqCode)

                    },
                    {
                        log(" " + it.localizedMessage)
                        view?.hideProgress()
                        view?.onError(it)
                    })
        )
    }

    fun verifyOtpRequest(reqCode: Int, requestVerifyOtp: RequestVerifyOtp) {
        view?.showProgress()
        disposable.add(
            api.requestVerifyOTP(requestVerifyOtp)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, reqCode)

                    },
                    {
                        log(" " + it.localizedMessage)
                        view?.hideProgress()
                        view?.onError(it)
                    })
        )
    }

    fun requestRegisterUser(reqCode: Int, emailBody: RequestBody, nameBody: RequestBody) {
        view?.showProgress()
        disposable.add(
            api.requestStoreUser(BaseApplication.instance.getCommonHeaders(),emailBody,nameBody)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, reqCode)

                    },
                    {
                        log(" " + it.localizedMessage)
                        view?.hideProgress()
                        view?.onError(it)
                    })
        )
    }

    fun loginUserCall(reqCode: Int, loginRequestModel: LoginRequestModel) {
        view?.showProgress()
        disposable.add(
            api.loginRequest(loginRequestModel)
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
}
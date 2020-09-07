/*
package code_setup.ui_.auth.views.authantication_

import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.annotation.RequiresApi
import code_setup.app_core.CoreActivity
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.auth.auth_mvp.AuthPresenter
import code_setup.ui_.auth.di_auth.AuthActivityModule
import code_setup.ui_.auth.di_auth.DaggerAuthActivityComponent
import code_setup.ui_.auth.views.EnterOTPActivity
import code_setup.ui_.home.views.HomeActivity
import com.burakeregar.githubsearch.home.presenter.AuthView
import com.electrovese.setup.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_forgot_layout.*
import kotlinx.android.synthetic.main.enter_otp_screen.*
import javax.inject.Inject


class ForgotActivity : CoreActivity(), AuthView {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResponse(listObj: Any, int: Int) {
        hideProgress()
        when (int) {
            NetworkRequest.REQUEST_LOGIN_CODE -> {
                var responseData = listObj as LoginResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    Prefs.putBoolean(CommonValues.IS_LOGEDIN, true)
                    Prefs.putString(CommonValues.ACCESS_TOKEN, responseData.response_obj.token)
                    Prefs.putString(
                        CommonValues.USER_DATA,
                        Gson().toJson(responseData.response_obj)
                    )

                    activitySwitcher(
                        this,
                        HomeActivity::class.java,
                        null
                    )
                    finishAffinity()
                } else if (responseData.response_code == NetworkCodes.FAIL.nCodes) {
                    AppUtils.showSnackBar(this@ForgotActivity, responseData.response_message)
                } else {
                    AppUtils.showSnackBar(this@ForgotActivity, responseData.response_message)
                }
            }
        }
    }

    override fun showProgress() {
        forgotLoaderView.show()
        contButton.visibility = View.INVISIBLE
    }

    override fun hideProgress() {
        forgotLoaderView.hide()
        contButton.visibility = View.VISIBLE
    }

    override fun noResult() {
    }

    @Inject
    lateinit var presenter: AuthPresenter

    override fun onActivityInject() {
        DaggerAuthActivityComponent.builder().appComponent(getAppcomponent())
            .authActivityModule(AuthActivityModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    override fun getScreenUi(): Int {
        return R.layout.activity_forgot_layout
    }

    override fun onError(it: Throwable) {
        hideProgress()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contButton.setSafeOnClickListener {
            validateBeforeGo()
        }
        backBtn.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun validateBeforeGo() {
        AppUtils.hideKeyboard(contButton)
        if (validated()) {
            showProgress()
            Handler().postDelayed(Runnable {
                hideProgress()
                activitySwitcher(this, EnterOTPActivity::class.java, null)

//                onBackPressed()
            }, 1000)
            */
/*  presenter.loginUserCall(
                  NetworkRequest.REQUEST_LOGIN_CODE,
                  LoginRequestModel(
                      emailLoginField.text.toString(),
                      passwordLoginField.text.toString()
                  )
              )*//*

        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun validated(): Boolean {
        */
/* if (emailForgotField.text.toString() == null || emailForgotField.text.toString().equals("") *//*
*/
/*|| !AppUtils.isValidEmail(
                 emailLoginField.text.toString()
             )*//*
*/
/*
        )*//*

        if (emailForgotField.text.toString() == null || !emailForgotField.text.isNotEmpty()) {
            AppUtils.showSnackBar(this, getString(R.string.error_enter_username))
            return false
        }

        return true
    }

}*/

/*
package code_setup.ui_.auth.views

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.KeyEvent
import android.view.View
import androidx.annotation.RequiresApi
import code_setup.app_core.BaseApplication
import code_setup.app_core.CoreActivity
import code_setup.app_models.request_.LoginRequestModel
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.auth.auth_mvp.AuthPresenter
import code_setup.ui_.auth.di_auth.AuthActivityModule
import code_setup.ui_.auth.di_auth.DaggerAuthActivityComponent
import code_setup.ui_.home.views.HomeActivity
import com.burakeregar.githubsearch.home.presenter.AuthView
import com.electrovese.setup.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_login_layout.*
import javax.inject.Inject


class LoginActivity : CoreActivity(), AuthView {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onResponse(listObj: Any, int: Int) {
        hideProgress()
        when (int) {
            NetworkRequest.REQUEST_LOGIN_CODE -> {
                var responseData = listObj as LoginResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    Prefs.putBoolean(CommonValues.IS_LOGEDIN, true)
                    Prefs.putString(CommonValues.ACCESS_TOKEN, responseData.response_obj.token)
                    Prefs.putString(CommonValues.USER_DATA, Gson().toJson(responseData.response_obj))

                    activitySwitcher(
                        this,
                        HomeActivity::class.java,
                        null
                    )
                    finishAffinity()
                } else if (responseData.response_code == NetworkCodes.FAIL.nCodes) {
                    AppUtils.showSnackBar(this@LoginActivity, responseData.response_message)
                } else {
                    AppUtils.showSnackBar(this@LoginActivity, responseData.response_message)
                }
            }
        }
    }

    override fun showProgress() {
        loginLoaderView.show()
        loginButton.visibility = View.INVISIBLE
    }

    override fun hideProgress() {
        loginLoaderView.hide()
        loginButton.visibility = View.VISIBLE
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
        return R.layout.activity_login_layout
    }

    override fun onError() {
        hideProgress()
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        BaseApplication.instance.generateFirebaseToken(this)
        loginButton.setOnClickListener {

            validateBeforeGo()
        }
        forgotPasswordBtn.setOnClickListener {
            activitySwitcher(this, ForgotActivity::class.java, null)
        }
        passwordLoginField.setOnClickListener {
            if (passwordLoginField.getInputType() === InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                passwordLoginField.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                passwordLoginField.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    R.mipmap.ic_eye_off,
                    0
                )
            } else {
                passwordLoginField.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD)
                passwordLoginField.setCompoundDrawablesWithIntrinsicBounds(
                    0,
                    0,
                    com.electrovese.setup.R.mipmap.ic_eyeview,
                    0
                )
            }

            passwordLoginField.setSelection(passwordLoginField.getText().length)
        }
        passwordLoginField.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.getAction() === KeyEvent.ACTION_DOWN) {
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            validateBeforeGo()
                            return true
                        }
                        else -> {
                        }
                    }
                }
                return false
            }
        })
        setSpannableLink()
    }

    private fun setSpannableLink() {
       AppUtils.spannableTextLink(this,bottomLinkTxt,"This is the app for Fresh Basket Delivery Person. If you are looking to order, Click here")
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun validateBeforeGo() {
        AppUtils.hideKeyboard(loginButton)
        if (validated()) {
            showProgress()
            presenter.loginUserCall(
                NetworkRequest.REQUEST_LOGIN_CODE,
                LoginRequestModel(
                    "+" + ccp.selectedCountryCode + " " + numberLoginField.text.toString(),
                    passwordLoginField.text.toString()
                )
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun validated(): Boolean {
        if (numberLoginField.text.toString() == null || numberLoginField.text.toString().equals("")
            || numberLoginField.text.toString().length < 10 */
/*|| !AppUtils.isValidEmail(
                 emailLoginField.text.toString()
             )*//*

        ) {
            AppUtils.showSnackBar(this, getString(R.string.error_enter_mobile_number))
            return false
        }
        if (passwordLoginField.text.toString() == null || passwordLoginField.text.toString().equals(
                ""
            )
        ) {
            AppUtils.showSnackBar(this, getString(R.string.error_enter_password))
            return false
        }
        return true
    }

}*/

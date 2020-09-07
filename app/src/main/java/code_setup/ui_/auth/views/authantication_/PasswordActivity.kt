package code_setup.ui_.auth.views.authantication_

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.transition.Slide
import android.util.Log
import android.view.Gravity.RIGHT
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import code_setup.app_core.BaseApplication
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.location_utils.log
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.auth.auth_mvp.AuthPresenter
import code_setup.ui_.auth.di_auth.AuthActivityModule
import code_setup.ui_.auth.di_auth.DaggerAuthActivityComponent
import code_setup.ui_.auth.models.request_model.RequestOTPModel
import code_setup.ui_.auth.models.request_model.RequestSocialLogin
import code_setup.ui_.auth.models.request_model.RequestVerifyOtp
import code_setup.ui_.auth.models.response_model.RequestOTPResponse
import code_setup.ui_.auth.models.response_model.VerifyOTPResponse
import code_setup.ui_.home.views.HomeActivity
import com.base.mvp.BasePresenter
import com.burakeregar.githubsearch.home.presenter.AuthView
import com.electrovese.setup.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_password.*
import java.text.DecimalFormat
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class PasswordActivity : AppCompatActivity(), AuthView {

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password)
        onActivityInject()
        mobileNumberTxt.setText(
            "+" + intent.getStringExtra(CommonValues.COUNTRY_CODE) + " " +
                    intent.getStringExtra(CommonValues.PHONE_NUMBER)
        )
        AppUtils.spannableTextOtpScreen(mobileNumberTxt, mobileNumberTxt.text.toString())
        requestOtp()
        animateViews()

        ivback.setOnClickListener {
            onBackPressed()
        }
        floatingBtn.setOnClickListener {
            /*val imm = getSystemService(
                Context.INPUT_METHOD_SERVICE
            ) as InputMethodManager
            imm.hideSoftInputFromWindow(etPass!!.windowToken, 0)

            Handler().postDelayed({
                val intent = Intent(this@PasswordActivity, SignupActivity::class.java)
                startActivity(intent)
                finish()
            }, 500)*/

        }

        verifyBtnText.setOnClickListener {
            AppUtils.hideKeyboard(verifyBtnText)
            if (validated()) {

//                val intent = Intent(this@PasswordActivity, HomeActivity::class.java)
//                startActivity(intent)
//                finishAffinity()


                presenter.verifyOtpRequest(
                    NetworkRequest.REQUEST_VERIFY_OTP_CODE,
                    RequestVerifyOtp(
                        intent.getStringExtra(CommonValues.COUNTRY_CODE)
                        , intent.getStringExtra(CommonValues.PHONE_NUMBER), etPass.text.toString()
                    )
                )

            }
        }
        resendOtpBtn.setOnClickListener {
            requestOtp()
            countDounTimer()
        }
    }

    private lateinit var yourCountDownTimer: CountDownTimer
    private fun countDounTimer() {
        resendCountdown.visibility = View.VISIBLE
        resendOtpBtn.setTextColor(resources.getColor(R.color.colorGrey))
        resendOtpBtn.isClickable = false
        resendOtpBtn.isEnabled = false
        val sec = TimeUnit.SECONDS.toMillis(60000)
        Log.e(" startCountDounTimer ", "   " + sec);
        yourCountDownTimer = object : CountDownTimer(30000, 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val f = DecimalFormat("00")
                val sec = millisUntilFinished / 1000 % 60
                resendCountdown.setText("00:" + f.format(sec) + " sec")
                if (sec.toInt() == 0) {
                    yourCountDownTimer.onFinish()
                    yourCountDownTimer.cancel()
                }
            }

            override fun onFinish() {
                resendCountdown.setText("00:00 sec")
                resendCountdown.visibility = View.GONE
                resendOtpBtn.setTextColor(resources.getColor(R.color.colorPrimary))
                resendOtpBtn.isClickable = true
                resendOtpBtn.isEnabled = true
            }
        }.start()
        yourCountDownTimer.start()
    }

    private fun validated(): Boolean {
        if (!etPass.text.toString().isNotEmpty()) {
            AppUtils.showSnackBar(this, getString(R.string.str_error_enter_otp))
            return false
        }
        return true
    }

    private fun animateViews() {
        val enterSlide = Slide(RIGHT)
        enterSlide.duration = 700
        enterSlide.addTarget(R.id.llphone)
        enterSlide.interpolator = DecelerateInterpolator(2f)
        window.enterTransition = enterSlide

        val returnSlide = Slide(RIGHT)
        returnSlide.duration = 700
        returnSlide.addTarget(R.id.llphone)
        returnSlide.interpolator = DecelerateInterpolator()
        window.returnTransition = returnSlide
    }

    private fun requestOtp() {
        var socialData = if (getIntent().getBooleanExtra(CommonValues.IS_SOCIAL_LOGIN, false)) {
            getIntent().getSerializableExtra(CommonValues.SOCIAL_DATA_USER)
        } else RequestSocialLogin("", "", "", "")
        socialData = socialData as RequestSocialLogin



        presenter.requestOtp(
            NetworkRequest.REQUEST_OTP_CODE,
            RequestOTPModel(
                intent.getStringExtra(CommonValues.COUNTRY_CODE),
                intent.getStringExtra(CommonValues.PHONE_NUMBER),
                socialData.name,
                socialData.email,
                socialData.id,
                socialData.type
            )
        )
    }

    private fun onActivityInject() {
        DaggerAuthActivityComponent.builder().appComponent(BaseApplication.appComponent)
            .authActivityModule(AuthActivityModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }

    override fun onResponse(list: Any, int: Int) {
        log("onResponse" + " " + Gson().toJson(list))
        when (int) {
            NetworkRequest.REQUEST_OTP_CODE -> {
                var responseData = list as RequestOTPResponse
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    etPass.setText("7777")//for temp use
                    AppUtils.showSnackBar(this, getString(R.string.str_opt_sent))
                } else {
                    AppUtils.showSnackBar(this, responseData.response_message)
                }
            }
            NetworkRequest.REQUEST_VERIFY_OTP_CODE -> {
                var responseData = list as VerifyOTPResponse
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    Prefs.putBoolean(CommonValues.IS_LOGEDIN, true)
                    Prefs.putString(CommonValues.ACCESS_TOKEN, responseData.response_obj.token)
                    Prefs.putString(
                        CommonValues.USER_DATA, Gson().toJson(responseData.response_obj)
                    )
                    val intent = Intent(this@PasswordActivity, HomeActivity::class.java)
                    startActivity(intent)
                    finishAffinity()
                } else if (responseData.response_code == NetworkCodes.SUCCEES_REGISTER_USER_NOW.nCodes) {

                    Prefs.putString(CommonValues.ACCESS_TOKEN, responseData.response_obj.token)
                    val intent = Intent(this@PasswordActivity, SignupActivity::class.java)
                    intent.putExtra(
                        CommonValues.PHONE_NUMBER,
                        responseData.response_obj.country_code + " " + responseData.response_obj.contact
                    )
                    if (getIntent().getBooleanExtra(CommonValues.IS_SOCIAL_LOGIN, false)) {
                        intent.putExtra(
                            CommonValues.SOCIAL_DATA_USER,
                            getIntent().getSerializableExtra(CommonValues.SOCIAL_DATA_USER)
                        )
                        intent.putExtra(CommonValues.IS_SOCIAL_LOGIN, true)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    AppUtils.showSnackBar(this, responseData.response_message)
                }
            }
        }
    }

    override fun showProgress() {
        otpLoaderView.show()
    }

    override fun hideProgress() {
        otpLoaderView.hide()
    }

    override fun noResult() {

    }

    override fun onError(it: Throwable?) {

    }

    override fun onError() {

    }

    @Inject
    lateinit var presenter: AuthPresenter

    override fun setPresenter(presenter: BasePresenter<*>) {

    }
}

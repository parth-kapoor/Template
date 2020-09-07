/*
package code_setup.ui_.auth.views

import android.animation.TimeInterpolator
import android.content.Intent
import android.icu.lang.UCharacter.IndicPositionalCategory.RIGHT
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.transition.Slide
import android.view.View
import android.view.animation.DecelerateInterpolator
import androidx.annotation.RequiresApi
import code_setup.app_core.CoreActivity
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.ui_.auth.di_auth.AuthActivityModule
import code_setup.ui_.auth.di_auth.DaggerAuthActivityComponent
import code_setup.ui_.auth.auth_mvp.AuthPresenter
import com.burakeregar.githubsearch.home.presenter.AuthView
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.activity_password.*
import java.text.DecimalFormat
import javax.inject.Inject


class OTPActivity : CoreActivity(), AuthView {

    private lateinit var yourCountDownTimer: CountDownTimer
    val TAG: String = OTPActivity::class.java.simpleName
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
        return R.layout.activity_password
    }

    override fun onResponse(list: Any, int: Int) {

*/
/*
        when (int) {
            NetworkRequest.REQUEST_OTP_CODE -> {
                var bResponse = list as BaseResponseModel
                verifyBtnText.setText(R.string.str_verify_now)
                verifyBtn.isClickable = true
                verifyBtn.isPressed = false
                if (bResponse.response_code == NetworkConstant.SUCCESS) {
                    AppUtils.showToast(bResponse.response_message)
                } else {
                    AppUtils.showToast(bResponse.response_message)
                }
            }
            NetworkRequest.REQUEST_VERIFY_OTP_CODE -> {
                var bResponse = list as UserResponseModel
                if (bResponse.response_code == NetworkConstant.SUCCESS) {
                    *//*
*/
/*if (LoginWithPhone.getInstance() != null) {
                        LoginWithPhone.getInstance()!!.finish()
                    }*//*
*/
/*
                    Handler().postDelayed({
                        etPass!!.isCursorVisible = false
                        rootFrame!!.alpha = 0.4f
                        val intent = Intent(this@OTPActivity, HomeActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(intent)
                        finishAffinity()
                    }, 100)
                } else if (bResponse.response_code == NetworkConstant.USER_NOT_REGISTERED) {
                    Handler().postDelayed({
                        var bundle: Bundle = Bundle()
                        bundle.putString(
                            CommonValues.PHONE_NUMBER,
                            intent.getStringExtra(CommonValues.PHONE_NUMBER)
                        )
                        bundle.putString(
                            CommonValues.COUNTRY_CODE,
                            intent.getStringExtra(CommonValues.COUNTRY_CODE)
                        )

                        bundle.putSerializable(
                            CommonValues.USER_DATA,
                            intent.getSerializableExtra(CommonValues.USER_DATA)
                        )
                        activitySwitcher(this@OTPActivity, SignupAct::class.java, bundle)
                        finish()

                    }, 100)
                } else {
                    AppUtils.showToast(bResponse.response_message)
                }
            }
        }*//*


    }

    override fun showProgress() {
//        showLoading()

        progressLoaderOtp.visibility = View.VISIBLE
        progressLoaderOtp.bringToFront()
        verifyBtn.isClickable = false
        verifyBtn.isPressed = true
    }

    override fun hideProgress() {
//        closeLoading()

        progressLoaderOtp.visibility = View.VISIBLE
        progressLoaderOtp.bringToFront()
        verifyBtn.isClickable = true
        verifyBtn.isPressed = false
    }

    override fun noResult() {

    }

    override fun onError(it: Throwable?) {

    }

    override fun onError() {
//        closeLoading()
        progressLoaderOtp.visibility = View.VISIBLE
        verifyBtn.isClickable = true
        verifyBtn.isPressed = false
//        AppUtils.showToast(getString(R.string.str_error))
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val enterSlide = Slide(RIGHT)
        enterSlide.duration = 700
        enterSlide.addTarget(R.id.llphone)
        enterSlide.interpolator = DecelerateInterpolator(2f) as TimeInterpolator?
        window.enterTransition = enterSlide

        val returnSlide = Slide(RIGHT)
        returnSlide.duration = 700
        returnSlide.addTarget(R.id.llphone)
        returnSlide.interpolator = DecelerateInterpolator()
        window.returnTransition = returnSlide
        ivback.setOnClickListener {
            onBackPressed()
        }
        getIntentData(intent)


        verifyBtn.setOnClickListener {
            if (allValidated())
                verifyOtpRequest()

        }
        resendOtpBtn.setSafeOnClickListener {
            getOtpRequest()
            countDounTimer()
        }
    }

    private fun allValidated(): Boolean {
        if (etPass.text.toString() == null || etPass.text.toString().equals("")) {
            AppUtils.showToast(getString(R.string.error_str_enter_otp))
            return false
        }
        return true
    }


    private var mobileNumber: String? = null
    private var countryCode: String? = null
    var isFromSocial: Boolean = false
    private fun getIntentData(intent: Intent) {
        mobileNumber = intent.getStringExtra(CommonValues.PHONE_NUMBER)
        countryCode = intent.getStringExtra(CommonValues.COUNTRY_CODE)
        mobileNumberTxt.text = countryCode + " " + mobileNumber
        getOtpRequest()

    }

    private fun verifyOtpRequest() {

        */
/*  var otpVerifyRequest: VerifyOtpRequestModel
                  if (isFromSocial) {
                      otpVerifyRequest =
                          VerifyOtpRequestModel(
                              mobileNumber!!.toLong(),
                              countryCode.toString(),
                              "7777",
                              "1"
                          )
                  } else {
                      otpVerifyRequest =
                          VerifyOtpRequestModel(mobileNumber!!.toLong(), countryCode.toString(), "7777", "")
                  }
          presenter.verifyOtpRequest(NetworkRequest.REQUEST_VERIFY_OTP_CODE, otpVerifyRequest)*//*


    }

    private fun getOtpRequest() {
*/
/*
        try {
            verifyBtnText.setText(R.string.str_requesting)
            verifyBtn.isClickable = false
            verifyBtn.isPressed = true
            var otpRequest: OtpRequestModel =
                OtpRequestModel(mobileNumber!!.toLong(), countryCode.toString())
            presenter.getRequestedOtp(NetworkRequest.REQUEST_OTP_CODE, otpRequest)
        } catch (e: Exception) {
            e.printStackTrace()
        }*//*


    }

    private fun countDounTimer() {
        resendCountdown.visibility = View.VISIBLE
        resendOtpBtn.setTextColor(resources.getColor(R.color.colorGrey))
        resendOtpBtn.isClickable = false
        resendOtpBtn.isEnabled = false
//        val sec = TimeUnit.SECONDS.toMillis(60000)
//        Log.e(" startCountDounTimer ", "   " + sec);
        yourCountDownTimer = object : CountDownTimer(60000, 1000) {

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

    */
/* @OnClick(R.id.fabProgressCircle)
     internal fun nextActivity() {
         etPass!!.isCursorVisible = false
         rootFrame!!.alpha = 0.4f
         fabProgressCircle!!.show()

         val imm = getSystemService(
             Context.INPUT_METHOD_SERVICE
         ) as InputMethodManager
         imm.hideSoftInputFromWindow(etPass!!.windowToken, 0)

         val postDelayed: Any = Handler().postDelayed({
             val intent = Intent(this@PasswordActivity, HomeActivity::class.java)
             startActivity(intent)
             finish()
         }, 1000)
     }
 *//*


}

*/

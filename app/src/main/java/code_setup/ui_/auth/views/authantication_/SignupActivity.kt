package code_setup.ui_.auth.views.authantication_

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import code_setup.ui_.auth.models.request_model.RequestSocialLogin
import code_setup.ui_.auth.models.response_model.StoreUserResponseModel
import code_setup.ui_.home.views.HomeActivity
import com.base.mvp.BasePresenter
import com.burakeregar.githubsearch.home.presenter.AuthView
import com.electrovese.setup.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.layout_signup_screen.*
import kotlinx.android.synthetic.main.signup_field_view.*
import okhttp3.MediaType
import okhttp3.RequestBody
import javax.inject.Inject

class SignupActivity : AppCompatActivity(), AuthView {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_signup_screen)
        onActivityInject()
        try {
            phoneNumberSignup.setText(getFormatedNumber(intent.getStringExtra(CommonValues.PHONE_NUMBER)))
            var socialData = if (getIntent().getBooleanExtra(CommonValues.IS_SOCIAL_LOGIN, false)) {
                getIntent().getSerializableExtra(CommonValues.SOCIAL_DATA_USER)
            } else RequestSocialLogin("", "", "", "")
            socialData = socialData as RequestSocialLogin
            if (socialData.name.isNotEmpty()) {
                fullNameSignup.setText(socialData.name)
                fullNameSignup.isEnabled = false
            }
            if (socialData.email.isNotEmpty()) {
                emailSignup.setText(socialData.email)
                emailSignup.isEnabled = false
            }
        } catch (e: Exception) {
        }
        ivbackSignup.setOnClickListener {
            onBackPressed()
        }
        signupBtnText.setOnClickListener {
            if (validated()) {
                presenter.requestRegisterUser(
                    NetworkRequest.REQUEST_REGISTER_USRE,
                    getEmailBody(),
                    getNameBody()
                )
            }
        }
        setSpannaple()
    }

    private fun getFormatedNumber(stringExtra: String): String {
        var formatedString: String =
            "(+" + stringExtra.split(" ")[0] + ") " + stringExtra.split(" ")[1]
        return formatedString
    }

    private fun getEmailBody(): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), emailSignup.text.toString())
    }

    private fun getNameBody(): RequestBody {
        return RequestBody.create(MediaType.parse("text/plain"), fullNameSignup.text.toString())
    }


    private fun validated(): Boolean {
        if (!fullNameSignup.text.toString().isNotEmpty()) {
            AppUtils.showSnackBar(this, getString(R.string.str_error_enter_full_name))
            return false
        } else if (!emailSignup.text.toString().isNotEmpty()) {
            AppUtils.showSnackBar(this, getString(R.string.str_error_email))
            return false
        } else if (!AppUtils.isValidEmail(emailSignup.text.toString())) {
            AppUtils.showSnackBar(this, getString(R.string.str_error_email))
            return false
        } else if (!signUpCheckBoxterms.isChecked) {
            AppUtils.showSnackBar(this, getString(R.string.str_error_terms))
            return false
        }
        return true
    }

    private fun onActivityInject() {
        DaggerAuthActivityComponent.builder().appComponent(BaseApplication.appComponent)
            .authActivityModule(AuthActivityModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    private fun setSpannaple() {
        AppUtils.spannableTextTerms(signUpCheckBoxtermsTxt, "", this@SignupActivity)
    }

    override fun onBackPressed() {

    }

    override fun onResponse(list: Any, int: Int) {
        log("onResponse" + " " + Gson().toJson(list))
        when (int) {
            NetworkRequest.REQUEST_REGISTER_USRE -> {
                var responseData = list as StoreUserResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    Prefs.putBoolean(CommonValues.IS_LOGEDIN, true)
                    Prefs.putString(
                        CommonValues.USER_DATA,
                        Gson().toJson(responseData.response_obj)
                    )
                    startActivity(Intent(this, HomeActivity::class.java))
                    finishAffinity()
                } else {
                    AppUtils.showSnackBar(this, responseData.response_message)
                }
            }
        }

    }

    override fun showProgress() {
        signupLoaderView.visibility = View.VISIBLE
        signupLoaderView.show()
        signupBtnText.visibility = View.INVISIBLE
    }

    override fun hideProgress() {
        signupLoaderView.visibility = View.GONE
        signupLoaderView.hide()
        signupBtnText.visibility = View.VISIBLE
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
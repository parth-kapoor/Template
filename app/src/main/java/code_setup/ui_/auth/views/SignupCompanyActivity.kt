package code_setup.ui_.auth.views

import android.os.Bundle
import code_setup.app_core.CoreActivity
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.activity_signup_company_layout.*
import kotlinx.android.synthetic.main.common_toolbar_lay.*

class SignupCompanyActivity : CoreActivity() {
    override fun onActivityInject() {

    }

    override fun getScreenUi(): Int {
        return R.layout.activity_signup_company_layout
    }

    override fun onError() {


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txtTitletoolbar.setText(R.string.str_signup)
        backToolbar.setOnClickListener {
            onBackPressed()
        }
        signUpCmpnyButton.setSafeOnClickListener {

        }
    }
}
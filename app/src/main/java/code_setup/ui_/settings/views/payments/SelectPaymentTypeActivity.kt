package code_setup.ui_.settings.views.payments

import android.os.Bundle
import android.view.View
import code_setup.app_core.CoreActivity
import code_setup.app_util.AnimUtils
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.common_toolbar_lay.*
import kotlinx.android.synthetic.main.common_toolbar_with_appbar.*

class SelectPaymentTypeActivity : CoreActivity() {
    override fun onActivityInject() {

    }

    override fun getScreenUi(): Int {

        return R.layout.select_payment_type_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backToolbar, false)
        AnimUtils.moveAnimationX(backToolbar, true)
        txtTitletoolbar.setText(R.string.str_payment_options)
        val function: (View) -> Unit = {
            AppUtils.hideKeyboard(backToolbar)
            onBackPressed()
        }
        backToolbar.setOnClickListener(function)
    }

    override fun onBackPressed() {
        setResult(CommonValues.REQUEST_CODE_LOCATION_PICKER)
        super.onBackPressed()

    }

    override fun onError() {

    }
}
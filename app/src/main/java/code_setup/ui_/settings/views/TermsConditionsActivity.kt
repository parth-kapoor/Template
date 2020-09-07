package code_setup.ui_.settings.views

import android.os.Bundle
import code_setup.app_core.CoreActivity
import code_setup.app_util.AnimUtils
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.common_toolbar_lay.*

class TermsConditionsActivity : CoreActivity() {
    override fun onActivityInject() {

    }

    override fun getScreenUi(): Int {
        return R.layout.layout_terms_conditions_screen
    }

    override fun onError() {
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backToolbar, false)
        AnimUtils.moveAnimationX(backToolbar, true)
        toolbar_root.setBackgroundColor(resources.getColor(R.color.colorWhite))
        txtTitletoolbar.setTextColor(resources.getColor(R.color.colorTextLabel))
        backToolbar.setColorFilter(getResources().getColor(R.color.colorTextLabel))
        txtTitletoolbar.setText(R.string.strProfile)
//        AppUtils.statusBarIconTheme(this, true)
        backToolbar.setOnClickListener {
            onBackPressed()
        }
        if (intent.getStringExtra(CommonValues.SCREEN_TYPE).equals(CommonValues.TERMS)) {
            txtTitletoolbar.setText(R.string.strTermsConditions)
        } else if (intent.getStringExtra(CommonValues.SCREEN_TYPE).equals(CommonValues.POLICY)) {
            txtTitletoolbar.setText(R.string.strPrivacyPolicy)
        } else if (intent.getStringExtra(CommonValues.SCREEN_TYPE).equals(CommonValues.REFUND)) {
            txtTitletoolbar.setText(R.string.strRefundPolicy)
        }
    }

}
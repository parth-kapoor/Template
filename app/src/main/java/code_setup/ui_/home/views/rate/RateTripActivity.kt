package code_setup.ui_.home.views.rate

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.base.mvp.BasePresenter
import code_setup.app_core.CoreActivity
import code_setup.app_models.response_.BaseResponseModel
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.CommonValues
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.home.views.HomeActivity
import code_setup.ui_.settings.di_settings.DaggerSettingsComponent
import code_setup.ui_.settings.di_settings.SettingsModule
import code_setup.ui_.settings.settings_mvp.SettingsPresenter
import code_setup.ui_.settings.settings_mvp.SettingsView
import com.electrovese.setup.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.common_toolbar_lay.*
import kotlinx.android.synthetic.main.layout_rate_trip_activity.*
import kotlinx.android.synthetic.main.view_full_thanku_screen.*
import javax.inject.Inject

class RateTripActivity : CoreActivity(), SettingsView {
    lateinit var tourId: String
    @Inject
    lateinit var presenter: SettingsPresenter

    override fun onActivityInject() {
        DaggerSettingsComponent.builder().appComponent(getAppcomponent())
            .settingsModule(SettingsModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    override fun onResponse(list: Any, int: Int) {
        Log.e("onResponse", " " + Gson().toJson(list))

    }

    override fun showProgress() {

    }

    override fun hideProgress() {

    }

    override fun noResult() {

    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.layout_rate_trip_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txtTitletoolbar.setText(R.string.str_Review_your_trip)
        getintentData(intent)

        continueBtn.setOnClickListener {
            onBackPressed()
        }

        backToolbar.setOnClickListener {
            onBackPressed()
        }
    }

    private fun getintentData(intent: Intent) {
//        tourId = intent.getStringExtra(CommonValues.TOUR_ID)
//        var iData = intent.getSerializableExtra(CommonValues.TOUR_DATA)
//
//        if (iData != null) {
//            var tData = iData as OrderDetailResponseModel.ResponseObj
//        }

    }

    override fun onBackPressed() {
        super.onBackPressed()
        activitySwitcher(this, HomeActivity::class.java, null)
        finishAffinity()
    }

    override fun onResume() {
        super.onResume()

    }

}
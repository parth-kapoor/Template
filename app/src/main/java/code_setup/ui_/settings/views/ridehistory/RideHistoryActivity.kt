package code_setup.ui_.settings.views.ridehistory

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.mvp.BasePresenter
import code_setup.app_core.CoreActivity
import code_setup.app_models.request_.BookingHistoryRequest
import code_setup.app_models.response_.RideHistoryResponseModel
import code_setup.app_util.AnimUtils
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.settings.adapter_.RideHistoryAdapter
import code_setup.ui_.settings.di_settings.DaggerSettingsComponent
import code_setup.ui_.settings.di_settings.SettingsModule
import code_setup.ui_.settings.settings_mvp.SettingsPresenter
import code_setup.ui_.settings.settings_mvp.SettingsView
import com.electrovese.setup.R
import com.google.gson.Gson
import kotlinx.android.synthetic.main.common_toolbar_lay.*
import kotlinx.android.synthetic.main.common_toolbar_with_appbar.*
import kotlinx.android.synthetic.main.common_toolbar_with_appbar.toolbar_root
import kotlinx.android.synthetic.main.layout_ride_history_activity.*
import javax.inject.Inject

class RideHistoryActivity : CoreActivity(), SettingsView {
    lateinit var rideAdapter: RideHistoryAdapter
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
        when (int) {
            NetworkRequest.REQUEST_BOOKING_HISTORY -> {
                var responseData = list as RideHistoryResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {

//                    if (responseData.response_obj != null && responseData.response_obj.isNotEmpty()) {
//                        rideAdapter.updateAll(responseData.response_obj)
//                    }
//                    if (rideAdapter.itemCount > 0) {
//                        noHistoryTxt.visibility = View.GONE
//                        rideHistoryAdapter.visibility = View.VISIBLE
//                    } else {
//                        noHistoryTxt.visibility = View.VISIBLE
//                        rideHistoryAdapter.visibility = View.GONE
//                    }
                }
            }
        }

    }

    override fun showProgress() {
        loaderViewHistory.show()
    }

    override fun hideProgress() {
        loaderViewHistory.hide()
    }

    override fun noResult() {

    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.layout_ride_history_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backToolbar, false)
        AnimUtils.moveAnimationX(backToolbar, true)
        toolbar_root.setBackgroundColor(resources.getColor(R.color.colorWhite))
        txtTitletoolbar.setTextColor(resources.getColor(R.color.colorTextLabel))
        backToolbar.setColorFilter(getResources().getColor(R.color.colorTextLabel))
        txtTitletoolbar.setText(R.string.str_ride_history_and_schedule)
//        AppUtils.statusBarIconTheme(this, true)
        backToolbar.setOnClickListener {
            onBackPressed()
        }

        initAdapter()
        getHistory()
    }

    private fun getHistory() {
        presenter.getRideHistory(
            NetworkRequest.REQUEST_BOOKING_HISTORY,
            BookingHistoryRequest("COMPLETED")
        )
    }

    private fun initAdapter() {
        with(rideHistoryAdapter) {
            layoutManager = LinearLayoutManager(this@RideHistoryActivity)
            rideAdapter = RideHistoryAdapter(this@RideHistoryActivity,
                ArrayList(),
                object : OnItemClickListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any?) {

                    }

                })
            adapter = rideAdapter
        }
    }

    override fun onResume() {
        super.onResume()

    }

}
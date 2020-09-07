package code_setup.ui_.settings.views.ridehistory

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.base.mvp.BasePresenter
import code_setup.app_core.CoreActivity
import code_setup.app_util.AnimUtils
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.ui_.home.apapter_.RejectedRidesAdapter
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.common_toolbar_with_appbar.*
import kotlinx.android.synthetic.main.layout_ride_history_activity.*

class RejectedRidesActivity : CoreActivity() {
    override fun onActivityInject() {
    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.layout_rejected_rides_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backBtntoolbar, false)
        AnimUtils.moveAnimationX(backBtntoolbar, true)
        titleToolbar.setText(R.string.str_rejected_trips)
        backBtntoolbar.setOnClickListener {
            onBackPressed()
        }

        initAdapter()
    }

    private fun initAdapter() {
        with(rideHistoryAdapter) {
            layoutManager = LinearLayoutManager(this@RejectedRidesActivity)

            var rideAdapter: RejectedRidesAdapter = RejectedRidesAdapter(this@RejectedRidesActivity,
                ArrayList<Any>(), object : OnItemClickListener<Any> {
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
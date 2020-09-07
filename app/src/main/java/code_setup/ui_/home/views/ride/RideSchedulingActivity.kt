package code_setup.ui_.home.views.ride

import android.os.Bundle
import android.view.View
import code_setup.app_core.BaseApplication
import code_setup.app_core.CoreActivity
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_util.AnimUtils
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues.Companion.notificationModel
import code_setup.ui_.settings.views.payments.SelectPaymentTypeActivity
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.activity_schedule_ride_screen.*
import kotlinx.android.synthetic.main.common_toolbar_lay.*
import org.greenrobot.eventbus.EventBus

class RideSchedulingActivity : CoreActivity() {
    override fun onActivityInject() {

    }

    override fun getScreenUi(): Int {

        return R.layout.activity_schedule_ride_screen
    }

    override fun onError() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backToolbar, false)
        AnimUtils.moveAnimationX(backToolbar, true)
        txtTitletoolbar.setText(R.string.str_confirm_booking)
        val function: (View) -> Unit = {
            AppUtils.hideKeyboard(backToolbar)
            onBackPressed()
        }
        backToolbar.setOnClickListener(function)
        dateFieldTxt.text = BaseApplication.scheduledDate.split("at")[0]
        timeFieldTxt.text = BaseApplication.scheduledDate.split("at")[1]

        confirmScheduleBookingBtn.setOnClickListener {
            EventBus.getDefault().postSticky(CustomEvent<Any>(EVENTS.EVENT_BOOKING_SCHEDULED, true))
            onBackPressed()
        }

        changeBtnSchedule.setOnClickListener {
            activitySwitcher(this, SelectPaymentTypeActivity::class.java, null)
        }
    }
}
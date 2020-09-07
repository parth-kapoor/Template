package code_setup.ui_.settings

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import code_setup.app_core.CoreActivity
import code_setup.app_util.AnimUtils
import code_setup.app_util.AppUtils
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.common_toolbar_lay.*

class NotificationScreen : CoreActivity() {
    override fun onActivityInject() {

    }

    override fun getScreenUi(): Int {

        return R.layout.layout_notification_activity
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
        txtTitletoolbar.setText(R.string.strNotifications)
//        AppUtils.statusBarIconTheme(this, true)
        backToolbar.setOnClickListener {
            onBackPressed()
        }

    }

}
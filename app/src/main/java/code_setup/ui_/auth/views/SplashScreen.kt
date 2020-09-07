package code_setup.ui_.auth.views

import android.os.Bundle
import android.os.Handler
import code_setup.app_core.BaseApplication
import com.base.mvp.BasePresenter
import code_setup.app_core.CoreActivity
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.ui_.auth.views.authantication_.LoginActNew
import code_setup.ui_.home.views.HomeActivity
import com.electrovese.setup.R

class SplashScreen : CoreActivity() {
    override fun onActivityInject() {
    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.splach_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onResume() {
        super.onResume()
        BaseApplication.instance.generateFirebaseToken(this)
        moveNext()
    }


    private fun moveNext() {
        Handler().postDelayed({
            if (Prefs.getBoolean(CommonValues.IS_LOGEDIN, false)) {
                activitySwitcher(
                    this,
                    HomeActivity::class.java,
                    null
                )
            } else {
                activitySwitcher(
                    this,
                    LoginActNew::class.java,
                    null
                )
            }

        }, 3000)
    }

}
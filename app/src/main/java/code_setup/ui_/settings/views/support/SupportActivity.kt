package code_setup.ui_.settings.views.support

import android.os.Bundle
import android.view.View
import com.base.mvp.BasePresenter
import code_setup.app_core.CoreActivity
import code_setup.app_models.request_.RequestSupportModel
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_util.AnimUtils
import code_setup.app_util.AppDialogs
import code_setup.app_util.AppUtils
import code_setup.app_util.callback_iface.OnBottomDialogItemListener
import code_setup.net_.NetworkRequest
import code_setup.ui_.settings.di_settings.DaggerSettingsComponent
import code_setup.ui_.settings.di_settings.SettingsModule
import code_setup.ui_.settings.settings_mvp.SettingsPresenter
import code_setup.ui_.settings.settings_mvp.SettingsView
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.common_toolbar_with_appbar.*
import kotlinx.android.synthetic.main.layout_support_activity.*
import javax.inject.Inject

class SupportActivity : CoreActivity(), SettingsView {
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

    }

    override fun showProgress() {
        showLoading()
    }

    override fun hideProgress() {
        closeLoading()
    }

    override fun noResult() {

    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.layout_support_activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AnimUtils.moveAnimationX(backBtntoolbar, false)
        AnimUtils.moveAnimationX(backBtntoolbar, true)
        titleToolbar.setText(R.string.str_support)
        backBtntoolbar.setOnClickListener {
            onBackPressed()
        }
        chooseConcernBtn.setOnClickListener {
            AppDialogs.openSelectConcernAlert(
                this,
                Any(),
                Any(),
                object : OnBottomDialogItemListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any) {

                    }
                })
        }
        sendSupportBtn.setOnClickListener {
           /* if (validated()) {
                presenter.submitSupportRequest(
                    NetworkRequest.REQUEST_SUPORT, RequestSupportModel(
                        chooseConcernBtn.text.toString(),
                        messageText.text.toString()
                    )
                )
            }*/
            onBackPressed()
        }
        setUserData()
    }

    private fun validated(): Boolean {
        if (chooseConcernBtn.text.equals(getString(R.string.str_choose_concern))) {
            AppUtils.showSnackBar(this, getString(R.string.strErrorConcern))
            return false
        } else
            return true
    }

    private fun setUserData() {
        useraNameTxt.text = (getUserData() as LoginResponseModel.ResponseObj).name
        userEmaiil.text = (getUserData() as LoginResponseModel.ResponseObj).email
        userContactNo.text =
           "" + (getUserData() as LoginResponseModel.ResponseObj).contact
    }

    override fun onResume() {
        super.onResume()

    }

}
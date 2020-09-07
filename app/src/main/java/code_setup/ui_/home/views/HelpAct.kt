package code_setup.ui_.home.views

import android.os.Bundle
import code_setup.app_core.CoreActivity
import com.electrovese.setup.R
import com.base.mvp.BasePresenter
import kotlinx.android.synthetic.main.default_toolbar_lay.*


class HelpAct : CoreActivity() {
    override fun onActivityInject() {
    }

    override fun onError() {
    }

    override fun setPresenter(presenter: BasePresenter<*>) {
    }

    override fun getScreenUi(): Int {
        return R.layout.str_help_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        txt_title_toolbar.setText(R.string.str_help)
        toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

}
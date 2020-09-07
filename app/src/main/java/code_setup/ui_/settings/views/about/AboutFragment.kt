package code_setup.ui_.settings.views.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code_setup.app_core.CoreFragment
import code_setup.ui_.home.views.HomeActivity
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.trans_toolbar_lay.*

class AboutFragment : CoreFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_about_view, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        HomeActivity.homeInstance.txtToolbartitle.setText(R.string.str_about_app)
    }

    override fun onActivityInject() {

    }
}
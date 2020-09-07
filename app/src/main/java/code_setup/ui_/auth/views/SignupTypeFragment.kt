package code_setup.ui_.auth.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code_setup.app_core.CoreFragment
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.signup_type_fragment_layout.*

class SignupTypeFragment : CoreFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.signup_type_fragment_layout, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        indvidualBtn.setOnClickListener {
            activitySwitcher(
                activity!!,
                SignupActivity::class.java,
                null
            )
        }
        companyBtn.setOnClickListener {
            activitySwitcher(
                activity!!,
                SignupCompanyActivity::class.java,
                null
            )
        }
    }

    override fun onActivityInject() {

    }
}
package code_setup.ui_.home.views.earning

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code_setup.app_core.CoreFragment
import code_setup.ui_.home.views.HomeActivity
import kotlinx.android.synthetic.main.trans_toolbar_lay.*
import code_setup.app_util.AnimUtils
import code_setup.ui_.settings.views.ridehistory.RejectedRidesActivity
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.layout_earning_fragment.*


class EarningFragment : CoreFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_earning_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        HomeActivity.homeInstance.txtToolbartitle.setText(R.string.str_Earning)


        updateGraphView()


        weekBtn.setOnClickListener {
            weekBtn.setBackgroundResource(R.drawable.drawable_circle)
            weekBtn.setTextColor(resources.getColor(R.color.colorWhite))

            todayBtn.setBackgroundResource(R.color.colorWhite)
            todayBtn.setTextColor(resources.getColor(R.color.colorBlack))

            monthBtn.setBackgroundResource(R.color.colorWhite)
            monthBtn.setTextColor(resources.getColor(R.color.colorBlack))

        }
        todayBtn.setOnClickListener {
            todayBtn.setBackgroundResource(R.drawable.drawable_circle)
            todayBtn.setTextColor(resources.getColor(R.color.colorWhite))

            weekBtn.setBackgroundResource(R.color.colorWhite)
            weekBtn.setTextColor(resources.getColor(R.color.colorBlack))

            monthBtn.setBackgroundResource(R.color.colorWhite)
            monthBtn.setTextColor(resources.getColor(R.color.colorBlack))
        }
        monthBtn.setOnClickListener {
            monthBtn.setBackgroundResource(R.drawable.drawable_circle)
            monthBtn.setTextColor(resources.getColor(R.color.colorWhite))

            todayBtn.setBackgroundResource(R.color.colorWhite)
            todayBtn.setTextColor(resources.getColor(R.color.colorBlack))

            weekBtn.setBackgroundResource(R.color.colorWhite)
            weekBtn.setTextColor(resources.getColor(R.color.colorBlack))
        }


        rejectedTripsBtn.setOnClickListener {

            activity?.let { it1 -> activitySwitcher(it1, RejectedRidesActivity::class.java, null) }
        }
    }

    override fun onActivityInject() {

    }

    private fun updateGraphView() {
        AnimUtils.resizeView(textView, 1000, 10, 200)
        AnimUtils.resizeView(textView1, 1000, 10, 300)
        AnimUtils.resizeView(textView2, 1000, 10, 500)
        AnimUtils.resizeView(textView3, 1000, 10, 400)
        AnimUtils.resizeView(textView4, 1000, 10, 100)
        AnimUtils.resizeView(textView5, 1000, 10, 200)
        AnimUtils.resizeView(textView6, 1000, 10, 300)
    }
}
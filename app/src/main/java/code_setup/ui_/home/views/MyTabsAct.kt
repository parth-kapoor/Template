/*
package code_setup.ui_.home.views

import android.os.Bundle
import com.base.mvp.BasePresenter
import code_setup.app_core.CoreActivity
import code_setup.ui_.home.apapter_.TabAdapter
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.default_toolbar_lay.*
import kotlinx.android.synthetic.main.str_mytrips_layout.*


class MyTabsAct : CoreActivity() {
    override fun onActivityInject() {

    }

    override fun onError() {

    }

    override fun setPresenter(presenter: BasePresenter<*>) {

    }

    var adapter: TabAdapter? = null
    override fun getScreenUi(): Int {
        return R.layout.str_mytrips_layout
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initTabs()
        txt_title_toolbar.setText(R.string.str_my_tabs)
        toolbarBack.setOnClickListener {
            onBackPressed()
        }
    }

   */
/* private fun initTabs() {
        adapter = TabAdapter(supportFragmentManager)
        adapter!!.addFragment(PastFragment(), getString(R.string.str_past))
        adapter!!.addFragment(
            UpcomingFragment(), getString(R.string.str_upcomming)
        )
        viewPager.setAdapter(adapter)
        tabLayout.setupWithViewPager(viewPager)
    }*//*

}*/

package code_setup.ui_.home.home_mvp

import com.base.mvp.BaseView

interface HomeView : BaseView {
    fun onResponse(list: Any, int: Int)
    fun showProgress()
    fun hideProgress()
    fun noResult()
}


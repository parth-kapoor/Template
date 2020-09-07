package code_setup.ui_.settings.settings_mvp

import com.base.mvp.BaseView

interface SettingsView : BaseView {
    fun onResponse(list: Any, int: Int)
    fun showProgress()
    fun hideProgress()
    fun noResult()
}


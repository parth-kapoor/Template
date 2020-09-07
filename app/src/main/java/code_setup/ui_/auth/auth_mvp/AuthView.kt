package com.burakeregar.githubsearch.home.presenter

import com.base.mvp.BaseView

interface AuthView : BaseView {
    fun onResponse(list: Any, int: Int)
    fun showProgress()
    fun hideProgress()
    fun noResult()
    fun onError(it: Throwable?)
}


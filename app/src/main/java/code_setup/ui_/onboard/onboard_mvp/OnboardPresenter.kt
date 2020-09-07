package com.burakeregar.githubsearch.home.presenter

import android.app.Activity
import code_setup.app_models.other_.TourTypeModel
import com.base.mvp.BasePresenter
import com.base.util.SchedulerProvider
import com.electrovese.kotlindemo.networking.ApiInterface
import com.electrovese.setup.R
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class OnboardPresenter @Inject constructor(
    var api: ApiInterface,
    disposable: CompositeDisposable,
    scheduler: SchedulerProvider
) : BasePresenter<OnboardView>(disposable, scheduler) {

    fun getRepos(searchKey: String) {

        view?.showProgress()
        disposable.add(
            api.search(searchKey)
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, 1)

                    },
                    { _ ->
                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }

    /*fun getListData(requestCode: Int) {
        view?.showProgress()
        disposable.add(
            api.getList()
                .subscribeOn(scheduler.io())
                .observeOn(scheduler.ui())
                .subscribe(
                    { result ->
                        view?.hideProgress()
                        view?.onResponse(result, requestCode)

                    },
                    { _ ->
                        view?.hideProgress()
                        view?.onError()
                    })
        )
    }*/

    fun getTypesList(acT: Activity): ArrayList<TourTypeModel> {
        var tList = ArrayList<TourTypeModel>()
        tList.add(
            TourTypeModel(
                acT.getString(R.string.str_land),
                R.mipmap.ic_land_unselected,
                false
            )
        )
        tList.add(
            TourTypeModel(
                acT.getString(R.string.str_water),
                R.mipmap.ic_water_unselected,
                false
            )
        )
        tList.add(TourTypeModel(acT.getString(R.string.str_air), R.mipmap.ic_air_unselected, false))
        tList.add(
            TourTypeModel(
                acT.getString(R.string.str_walking),
                R.mipmap.ic_walking_unselected,
                false
            )
        )

        return tList
    }

    fun isTypeSelected(dataList: ArrayList<TourTypeModel>): Boolean {
        for (i in 0 until dataList.size) {
            if (dataList.get(i).isSelected) {
                return true
            }
        }
        return false

    }
}
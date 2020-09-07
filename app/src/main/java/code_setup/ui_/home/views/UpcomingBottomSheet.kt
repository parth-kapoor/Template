package code_setup.ui_.home.views

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import code_setup.app_core.CoreBottomSheetFragment
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_models.response_.TourListResponseModel
import code_setup.app_util.*
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.net_.NetworkCodes
import code_setup.net_.NetworkRequest
import code_setup.ui_.home.apapter_.UpcomingToursAdapter
import code_setup.ui_.home.di_home.DaggerHomeComponent
import code_setup.ui_.home.di_home.HomeModule
import code_setup.ui_.home.home_mvp.HomePresenter
import code_setup.ui_.home.home_mvp.HomeView
import code_setup.ui_.home.views.fragments.DeliveryUpdatesFragment
import com.base.mvp.BasePresenter
import com.electrovese.setup.R
import com.electrovese.setup.databinding.LayoutBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject
import kotlin.collections.ArrayList

class UpcomingBottomSheet : CoreBottomSheetFragment(), HomeView {
    private lateinit var desAdapter: UpcomingToursAdapter
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var bi: LayoutBottomSheetBinding? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val bottomSheet =
            super.onCreateDialog(savedInstanceState) as BottomSheetDialog

        val view =
            View.inflate(context, R.layout.layout_bottom_sheet, null)

        bi = DataBindingUtil.bind(view)
        bottomSheet.setContentView(view)
        bottomSheetBehavior =
            BottomSheetBehavior.from(view.parent as View)
        (bottomSheetBehavior as BottomSheetBehavior<View>?)!!.setPeekHeight(BottomSheetBehavior.PEEK_HEIGHT_AUTO)
        (bottomSheetBehavior as BottomSheetBehavior<View>?)!!.setHideable(true)//Important to add
//        (bottomSheetBehavior as BottomSheetBehavior<View>?)!!.isFitToContents=true
        bi!!.extraSpace.minimumHeight = screenHeight / 2
        (bottomSheetBehavior as BottomSheetBehavior<View>?)!!.setBottomSheetCallback(object :
            BottomSheetCallback() {
            override fun onStateChanged(view: View, i: Int) {
                if (BottomSheetBehavior.STATE_EXPANDED == i) {
                    showView(bi!!.appBarLayout, actionBarSize)
                    //hideAppBar(bi.profileLayout);
                    bi!!.seeallHolder.visibility = View.GONE
                    Log.d("onStateChanged", "  STATE_EXPANDED " + i)
                }
                if (BottomSheetBehavior.STATE_COLLAPSED == i) {
                    hideAppBar(bi!!.appBarLayout)
                    showView(bi!!.extraSpace, Companion.getActionBarSize(this@UpcomingBottomSheet))
                    if (desAdapter != null && desAdapter.itemCount > 0)
                        bi!!.seeallHolder.visibility = View.VISIBLE
                    bi!!.extraSpace.minimumHeight = screenHeight / 2
                    Log.d("onStateChanged", "  STATE_COLLAPSED " + i)
                }
                if (BottomSheetBehavior.STATE_HIDDEN == i) {
                    dismiss()
                    Log.d("onStateChanged", "  STATE_HIDDEN " + i)
                }
                if (BottomSheetBehavior.STATE_DRAGGING == i) {
                    Log.d("onStateChanged", "  STATE_DRAGGING " + i)
                }

            }

            override fun onSlide(view: View, v: Float) {

                Log.d("onSlide", "  " + v)
            }
        })
        bi!!.seeAllAppBtn.setOnClickListener {
            bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_EXPANDED
        }
        bi!!.cancelBtn.setOnClickListener {
            dismiss()
        }

        onActivityInject()
        initAdapter()
        hideAppBar(bi!!.appBarLayout)

        return bottomSheet
    }



    private fun initAdapter() {

        with(bi!!.upcominiToursFeagmentRecyclar) {
            desAdapter =
                UpcomingToursAdapter(activity!!, ArrayList(), object : OnItemClickListener<Any> {
                    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any?) {
                        if (!Prefs.getString(CommonValues.AVAIALBLE_SCANNED_VEHICLE_ID, "").equals(
                                other = ""
                            )
                        )
                            when (type) {
                                CommonValues.TOUR_PEOPLE_COUNT_CLICKED -> {

//                                AppDialogs.openDialogPeopleAlert(
//                                    activity!!,
//                                    t!!,
//                                    Any(),
//                                    object : OnBottomDialogItemListener<Any> {
//                                        override fun onItemClick(
//                                            view: View,
//                                            position: Int,
//                                            type: Int,
//                                            t: Any
//                                        ) {
//
//                                        }
//                                    })

                                }
                                CommonValues.TOUR_NAME_CLICKED -> {
                                    dismiss()
                                    HomeActivity.homeInstance.viewModes(
                                        VIEW_MODE.BOTTOM_FRAGMENT.nCodes,
                                        DeliveryUpdatesFragment()
                                    )
                                    Handler().postDelayed(Runnable {
                                        var positionData = t as TourListResponseModel.ResponseObj
                                        EventBus.getDefault().postSticky(
                                            CustomEvent<Any>(
                                                EVENTS.REQUEST_TOUR_DETAIL,
                                                positionData._id
                                            )
                                        )

                                    }, 100)
                                }
                            }
                        else {
                            AppUtils.showToast("You are offline.")
                        }

                    }
                })
            adapter = desAdapter
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        }
    }

    @Inject
    lateinit var presenter: HomePresenter

    override fun onActivityInject() {
        DaggerHomeComponent.builder().appComponent(getAppcomponent())
            .homeModule(HomeModule())
            .build()
            .inject(this)
        presenter.attachView(this)
    }

    override fun onStart() {
        super.onStart()
        bottomSheetBehavior!!.state = BottomSheetBehavior.STATE_COLLAPSED
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }


    @SuppressLint("RestrictedApi")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public fun onMessage(event: CustomEvent<Any>) {
        Log.d("onMessage", " " + event.type)
        when (event.type) {

        }
    }

    private fun hideAppBar(view: View) {
        val params = view.layoutParams
        params.height = 0
        view.layoutParams = params
    }

    private fun showView(view: View, size: Int) {
        val params = view.layoutParams
        params.height = size
        view.layoutParams = params
    }

    private val actionBarSize: Int
        private get() {
            val styledAttributes =
                context!!.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            return styledAttributes.getDimension(0, 0f).toInt()
        }

    companion object {
        val screenHeight: Int get() = getScreenHight()

        private fun getScreenHight(): Int {

            return Resources.getSystem().displayMetrics.heightPixels
        }

        private fun getActionBarSize(upcomingBottomSheet: UpcomingBottomSheet): Int {
            val styledAttributes =
                upcomingBottomSheet.context!!.theme.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
            return styledAttributes.getDimension(0, 0f).toInt()
        }
    }

    override fun onResponse(list: Any, int: Int) {
        Log.d("onResponse", "  " + Gson().toJson(list))
        when (int) {
            NetworkRequest.REQUEST_UPCOMING_TOURS -> {
                var responseData = list as TourListResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {

                    if (responseData?.response_obj != null && responseData.response_obj.isNotEmpty()) {
                        bi!!.noRideText.visibility = View.GONE
                        bi!!.noRidegif.visibility = View.GONE
                        bi!!.upcominiToursFeagmentRecyclar.visibility = View.VISIBLE
                        desAdapter.updateAll(responseData.response_obj)

                    } else {
                        bi!!.noRideText.visibility = View.VISIBLE
                        bi!!.upcominiToursFeagmentRecyclar.visibility = View.GONE
                        bi!!.seeallHolder.visibility = View.GONE
                        bi!!.noRidegif.visibility = View.VISIBLE
                    }

                }

            }
        }

    }

    override fun showProgress() {
        bi!!.upcominiToursFeagmentRecyclar.showShimmerAdapter()
    }

    override fun hideProgress() {
        bi!!.upcominiToursFeagmentRecyclar.hideShimmerAdapter()
    }

    override fun noResult() {

    }

    override fun onError() {
        bi!!.upcominiToursFeagmentRecyclar.hideShimmerAdapter()
    }

    override fun setPresenter(presenter: BasePresenter<*>) {

    }
}
/*
package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.AppDialogs
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.callback_iface.OnBottomDialogItemListener
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.destination_adapter_view.view.*


class DestinationListAdapter(
        internal var activity: androidx.fragment.app.FragmentActivity,
        val dataList: ArrayList<OrderDetailResponseModel.ResponseObj.Route>,
        internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<DestinationListAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.destination_adapter_view,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        (holder).bind(dataList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateAll(posts: List<OrderDetailResponseModel.ResponseObj.Route>) {
        this.dataList.clear()
        this.dataList.addAll(posts)
        notifyDataSetChanged()
    }

    fun addItem(posts: Object) {
        //        this.slotsList.add(0, posts);
        //        notifyDataSetChanged();
    }

    fun removeAll() {
        dataList.clear()
        notifyDataSetChanged()
    }


    inner class OptionViewHolder
        (view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(
                part: OrderDetailResponseModel.ResponseObj.Route,
                posit: Int,
                listener: OnItemClickListener<Any>
        ) = with(itemView) {

            checkBoxDestination.setText(part.name)
            if (part.bookings != null && part.bookings.size > 0) {
                txtPickUpTxt.setText(
                    activity.getString(R.string.str_pickup_) + " " + part.bookings.size + " " + activity.getString(
                        R.string.str_rider
                    )
                )
            } else {
                txtPickUpTxt.setText("")
            }
//            checkBoxDestination.isEnabled = false

            if (part.is_arrived) {
                checkBoxDestination.isChecked = true
                checkBoxDestination.isEnabled = false
                txtPickUpTxt.setTextColor(activity.resources.getColor(R.color.colorPrimary))
                txtPickUpTxt.setOnClickListener {
                    listener.onItemClick(
                        checkBoxDestination,
                        0,
                        CommonValues.TOUR_PICKUP_RIDER_CLICK,
                        dataList.get(posit)
                    )
                }
            } else {
                checkBoxDestination.isChecked = false
                checkBoxDestination.isEnabled = true
                txtPickUpTxt.setTextColor(activity.resources.getColor(R.color.colorTextGrey))
                checkBoxDestination.setOnClickListener {

                    if (!Prefs.getString(CommonValues.AVAIALBLE_SCANNED_VEHICLE_ID, "").equals(
                            other = ""
                        )
                    ) {
                        AppDialogs.openDialogArrivedMark(
                            activity,
                            Any(),
                            Any(),
                            object : OnBottomDialogItemListener<Any> {
                                override fun onItemClick(
                                    view: View,
                                    position: Int,
                                    type: Int,
                                    t: Any
                                ) {
                                    when (type) {
                                        CommonValues.TOUR_DESTINATION_ARRIVED -> {
                                            part.is_arrived = true
                                            listener.onItemClick(
                                                checkBoxDestination,
                                                0,
                                                CommonValues.TOUR_DESTINATION_ARRIVED,
                                                dataList.get(posit)
                                            )
                                            android.os.Handler().postDelayed(Runnable {
                                                notifyDataSetChanged()
                                            }, 1000)

                                        }
                                        0 -> {
                                            part.is_arrived = false
                                            checkBoxDestination.isChecked = false
                                            notifyDataSetChanged()
                                        }

                                    }
                                }

                            })
                    } else {
                        AppUtils.showToast("You are offline")
                        checkBoxDestination.isChecked = false
                    }

                }
            }

        }
    }

}
*/

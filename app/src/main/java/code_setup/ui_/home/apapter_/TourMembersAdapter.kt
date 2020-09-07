/*
package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.CommonValues
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_tour_people_view.view.*


class TourMembersAdapter(
        internal var activity: FragmentActivity,
        val dataList: ArrayList<OrderDetailResponseModel.ResponseObj.User>,
        val showPickUpStatus: Boolean,
        internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<TourMembersAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_tour_people_view,
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

    fun updateAll(posts: List<OrderDetailResponseModel.ResponseObj.User>) {
        this.dataList.clear();
        this.dataList.addAll(posts);
        notifyDataSetChanged();
    }

    fun addItem(posts: Object) {
        //        this.slotsList.add(0, posts);
        //        notifyDataSetChanged();
    }


    inner class OptionViewHolder
        (view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(
                part: OrderDetailResponseModel.ResponseObj.User,
                posit: Int,
                listener: OnItemClickListener<Any>
        ) = with(itemView) {
            if (showPickUpStatus) {
                if (part.is_board) {
                    memberPickUpStatus.setText(R.string.str_picked_up)
                    memberPickUpStatus.setBackgroundResource(R.drawable.rectangle_background_green)
                } else {
                    memberPickUpStatus.setText(R.string.str_pickup)
                    memberPickUpStatus.setBackgroundResource(R.drawable.rectangle_background)
                }
                memberPickUpStatus.visibility = View.VISIBLE
            } else {
                memberPickUpStatus.visibility = View.GONE
            }
            try {
                if (part.seats.toInt() <= 1) {
//                    memberSeats.setText(part.seats + " seat ")
                    memberSeats.setText(activity.getString(R.string.str_seat_booked) + " " + part.seats)
                } else {
//                    memberSeats.setText(part.seats + " seats ")
                    memberSeats.setText(activity.getString(R.string.str_seats_booked) + " " + part.seats)
                }
                memberSeats.visibility = View.VISIBLE
            } catch (e: Exception) {

            }
            memberName.setText(part.name)
            memberCallImg.setOnClickListener {
                listener.onItemClick(memberCallImg, 0, CommonValues.CALL_CLICK, dataList.get(posit))
            }
            memberMessageImg.setOnClickListener {
                listener.onItemClick(
                    memberMessageImg,
                    0,
                    CommonValues.MESSAGE_CLICK,
                    dataList.get(posit)
                )
            }
        }
    }

}
*/

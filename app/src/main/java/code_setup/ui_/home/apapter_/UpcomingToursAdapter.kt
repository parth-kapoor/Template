package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import code_setup.app_models.response_.TourListResponseModel
import code_setup.app_util.CommonValues
import code_setup.app_util.DateUtilizer
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_upcoming_tours_view.view.*
import kotlinx.android.synthetic.main.layout_tour_view_new.view.*


class UpcomingToursAdapter(
    internal var activity: FragmentActivity,
    val dataList: ArrayList<TourListResponseModel.ResponseObj>,
    internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<UpcomingToursAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_upcoming_tours_view,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        (holder).bind(dataList.get(position), position, listener)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun updateAll(posts: List<TourListResponseModel.ResponseObj>) {
        this.dataList.clear()
        this.dataList.addAll(posts)
        notifyDataSetChanged()
    }

    fun addItem(posts: Object) {
        //        this.slotsList.add(0, posts);
        //        notifyDataSetChanged();
    }


    inner class OptionViewHolder
        (view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        fun bind(
            part: TourListResponseModel.ResponseObj,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {
            tourCategory.setText(part.type.replace("_", " "))
            tourDestinationsTxt.setText(part.tour_name)
            tourPickupLocation.setText(part.start_location.name)


            updateDateView(tourDateText, part, posit)


            tourPickupTime.setText(
                DateUtilizer.getFormatedDate(
                    "HH:mm",
                    "hh:mm a",
                    part.booking_date.split(",")[1])+"( 30 Min Tour)"
            )
            if (part.seats > 1) {
                tourSeats.setText("" + part.seats + " " + activity.getString(R.string.seats))
            } else
                tourSeats.setText("" + part.seats + " " + activity.getString(R.string.seat))

            tourPickupDistance.setText("" + part.distance + " KM")
            tourPeopleTxt.setText("" + part.users.size)
            tourPickupLocation.isSelected = true
            tourPeopleTxt.setOnClickListener {
//                listener.onItemClick(
//                    tourPeopleTxt,
//                    posit,
//                    CommonValues.TOUR_PEOPLE_COUNT_CLICKED,
//                    dataList.get(posit)
//                )
            }
            holderViewTimeDistance.setOnClickListener {
                listener.onItemClick(
                    itemView,
                    posit,
                    CommonValues.TOUR_NAME_CLICKED,
                    dataList.get(posit)
                )
            }
            tourDestinationsTxt.setOnClickListener {
                listener.onItemClick(
                    itemView,
                    posit,
                    CommonValues.TOUR_NAME_CLICKED,
                    dataList.get(posit)
                )
            }
        }
    }

    private fun updateDateView(
        tourDateText: TextView,
        part: TourListResponseModel.ResponseObj,
        posit: Int
    ) {
        try {
            tourDateText.setText(DateUtilizer.getPreText("dd-MM-yyyy", part.booking_date)+ DateUtilizer.getFormatedDate("dd-MM-yyyy",
                    "dd MMMM yyyy",
                    part.booking_date.split(",")[0]
                )
            )
            if (posit > 0)
                if (DateUtilizer.getPreText("dd-MM-yyyy", part.booking_date).equals(DateUtilizer.getPreText("dd-MM-yyyy", dataList.get(posit - 1).booking_date))) {
                    tourDateText.visibility = View.GONE
                } else {
                    tourDateText.visibility = View.VISIBLE
                }

        } catch (e: Exception) {
            tourDateText.setText(
                DateUtilizer.getPreText(
                    "dd-MM-YYYY",
                    part.booking_date
                ) + DateUtilizer.getFormatedDate(
                    "dd-MM-YYYY",
                    "dd MMMM YYYY",
                    part.booking_date.split(",")[0]
                )
            )
            if (posit > 0) {
                if (DateUtilizer.getPreText(
                        "dd-MM-YYYY",
                        part.booking_date
                    ).equals(
                        DateUtilizer.getPreText(
                            "dd-MM-YYYY",
                            dataList.get(posit - 1).booking_date
                        )
                    )
                ) {
                    tourDateText.visibility = View.GONE
                } else {

                    tourDateText.visibility = View.VISIBLE
                }

            }

        }
    }

}

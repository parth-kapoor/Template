package code_setup.ui_.settings.adapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code_setup.app_models.response_.RideHistoryResponseModel
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_destination_list_view.view.*


class TourHistoryDestinationAdapter(
    internal var activity: FragmentActivity,
    val dataList: ArrayList<RideHistoryResponseModel.ResponseObj.Route>,
    internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<TourHistoryDestinationAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_destination_list_view,
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

    fun updateAll(posts: List<RideHistoryResponseModel.ResponseObj.Route>) {
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
            part: RideHistoryResponseModel.ResponseObj.Route,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {
            tripsDestinationsTxt.setText(part.name)
            if (posit == (dataList.size - 1)) {
                tripsDestinationsTxt.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.ic_ending_location_point,
                    0,
                    0,
                    0
                )
            } else {
                tripsDestinationsTxt.setCompoundDrawablesWithIntrinsicBounds(
                    R.mipmap.ic_starting_location_point,
                    0,
                    0,
                    0
                )
            }
        }
    }

}

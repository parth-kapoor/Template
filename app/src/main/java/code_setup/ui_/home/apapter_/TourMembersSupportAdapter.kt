package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_tour_members_support_view.view.*


class TourMembersSupportAdapter(
        internal var activity: FragmentActivity,
        val dataList: ArrayList<OrderDetailResponseModel.ResponseObj.User>,
        internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<TourMembersSupportAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_tour_members_support_view,
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
                part: OrderDetailResponseModel.ResponseObj.User,
                posit: Int,
                listener: OnItemClickListener<Any>
        ) = with(itemView) {

            riderName.setText(part.name)
            riderName.setOnClickListener {
                listener.onItemClick(riderName, 0, 0, dataList.get(posit))
            }
        }

    }

}

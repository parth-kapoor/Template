package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R


class RejectedRidesAdapter(
    internal var activity: FragmentActivity,
    val dataList: ArrayList<Any>,
    internal var listener: Any
) : androidx.recyclerview.widget.RecyclerView.Adapter<RejectedRidesAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_rejectted_ride_view,
                p0,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
//        (holder).bind(dataList[position], position, listener)
    }

    override fun getItemCount(): Int {
        return 3/*dataList.size*/
    }

    fun updateAll(posts: List<Any>) {
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
            part: Any,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {

        }
    }

}

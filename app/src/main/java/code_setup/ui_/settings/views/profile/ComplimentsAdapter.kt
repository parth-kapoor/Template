package code_setup.ui_.settings.views.profile

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code_setup.app_models.response_.ProfileResponseModel
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.compliments_adapter_view.view.*


class ComplimentsAdapter(
    internal var activity: Activity,
    val dataList: ArrayList<ProfileResponseModel.ResponseObj.Complement>,
    internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<ComplimentsAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.compliments_adapter_view,
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

    fun updateAll(posts: List<ProfileResponseModel.ResponseObj.Complement>) {
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
            part: ProfileResponseModel.ResponseObj.Complement,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {
            complimentCount.bringToFront()
            complimentTxt.setText(part.name)
            complimentCount.setText("" + part.count)
        }
    }

}

package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import code_setup.app_models.other_.CabsDataModel
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.ui_.home.models.PriceListResponseModel
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.cabs_adapter_view.view.*


class CabsListAdapter(
    internal var activity: androidx.fragment.app.FragmentActivity,
    val dataList: ArrayList<PriceListResponseModel.ResponseObj>,
    internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<CabsListAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.cabs_adapter_view,
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

    fun updateAll(posts: List<PriceListResponseModel.ResponseObj>) {
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
            part: PriceListResponseModel.ResponseObj,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {
            if (part.name.equals("sedan")) {
                cabImageVw.setImageResource(R.mipmap.ic_muv)
            } else {
                cabImageVw.setImageResource(R.mipmap.ic_hatchback)
            }
            seatsText.setText(part.capacity + " Person")
            cabPriceTxt.setText(activity.getString(R.string.str_rupeee_symbol) + "" + part.total_price)
            cabTypeTxt.setText(part.name)

            if (part.isSelected) {
                cabsDataHolderView.setBackgroundColor(resources.getColor(R.color.colorCabSelectionBg))
            } else {
                cabsDataHolderView.setBackgroundColor(resources.getColor(R.color.colorWhite))
            }
            itemView.setOnClickListener {
                if (part.isSelected) {
                    listener.onItemClick(itemView, posit, 0, Any())
                }
                onrefreshSelection(posit)
            }
        }
    }

    private fun onrefreshSelection(posit: Int) {
        for (i in 0 until dataList.size) {
            if (posit == i) {
                dataList.get(i).isSelected = true
            } else {
                dataList.get(i).isSelected = false
            }
        }
        notifyDataSetChanged()
    }

    fun getPositionData(position: Int): PriceListResponseModel.ResponseObj {
        return dataList.get(position)
    }


    fun getSelectedCabData(): String {
        for (i in 0 until dataList.size) {
            if (dataList.get(i).isSelected) {
                return dataList.get(i).category_id
            }
        }
        return ""
    }

}

/*
package code_setup.ui_.onboard.adapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code_setup.app_models.other_.TourTypeModel
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_view_tour_type.view.*


class TourTypeAdapter(
    internal var activity: FragmentActivity,
    val dataList: ArrayList<TourTypeModel>,
    internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<TourTypeAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_view_tour_type,
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

    fun updateAll(posts: List<TourTypeModel>) {
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
            part: TourTypeModel,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {
            typeText.setText(part.type)
            if (part.isSelected) {
                when (part.type) {
                    context.getString(R.string.str_air) -> {
                        typeImage.setBackgroundResource(R.mipmap.ic_air_selected)
                        selectionImageview.setBackgroundResource(R.mipmap.ic_category_selected)
                    }
                    context.getString(R.string.str_walking) -> {
                        typeImage.setBackgroundResource(R.mipmap.ic_walking_selected)
                        selectionImageview.setBackgroundResource(R.mipmap.ic_category_selected)
                    }
                    context.getString(R.string.str_land) -> {
                        typeImage.setBackgroundResource(R.mipmap.ic_land_selected)
                        selectionImageview.setBackgroundResource(R.mipmap.ic_category_selected)
                    }
                    context.getString(R.string.str_water) -> {
                        typeImage.setBackgroundResource(R.mipmap.ic_water_selected)
                        selectionImageview.setBackgroundResource(R.mipmap.ic_category_selected)
                    }
                }
                typeText.setTextColor(resources.getColor(R.color.colorPrimary))
            } else {
                typeImage.setBackgroundResource(part.icon)
                selectionImageview.setBackgroundResource(R.mipmap.ic_category_unselected)
                typeText.setTextColor(resources.getColor(R.color.colorTextGrey))
            }


            selectionView.setOnClickListener {
                refreshView(posit)
                listener.onItemClick(selectionView, posit, 0, dataList.get(posit))
            }
        }
    }

    private fun refreshView(posit: Int) {
        android.os.Handler().postDelayed({
            for (x in 0 until dataList.size)// println(x) // Prints 0 through 9
            {
                if (posit == x) {
                    dataList.get(x).isSelected = true
                } else dataList.get(x).isSelected = false
            }
            notifyDataSetChanged()

        }, 10)
    }

}
*/

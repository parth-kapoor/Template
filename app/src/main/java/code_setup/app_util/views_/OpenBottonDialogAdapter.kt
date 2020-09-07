package code_setup.app_util.views_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import code_setup.app_util.CommonValues
import code_setup.app_util.callback_iface.OnItemClickListener
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.view_bottom_dialog.view.*


class OpenBottonDialogAdapter(
    internal var nameList: Array<String>,
    internal var iconsList: Array<Int>,
    internal var listener: OnItemClickListener<*>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.view_bottom_dialog, parent, false)
        return CommonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as CommonViewHolder

        (viewHolder).bind(nameList[position], position, listener)

    }

    override fun getItemCount(): Int {
        return nameList.size
    }

    inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(s: String, position: Int, listener: OnItemClickListener<*>) = with(itemView) {
            if (iconsList.size == 0) {
                txt_name!!.text = nameList[position]
                icon!!.visibility = View.GONE
            } else {
                txt_name!!.text = nameList[position]
                icon!!.setBackgroundResource(iconsList[position])
            }
            itemView.setOnClickListener(View.OnClickListener {

                listener.onItemClick(
                    itemView,
                    adapterPosition,
                    CommonValues.APAPTER_BOTTOM_DIALOG_CLICK,
                    nameList[adapterPosition]
                )
            })
        }


    }
}
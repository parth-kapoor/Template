package code_setup.ui_.home.apapter_

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import code_setup.app_util.AppUtils
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.ui_.home.models.GooglePlaceResponseModel
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_google_place_view.view.*


class GooglePlaceAdapter(
    internal var activity: FragmentActivity,
    val dataList: ArrayList<GooglePlaceResponseModel.Prediction>,
    internal var listener: OnItemClickListener<Any>
) : RecyclerView.Adapter<GooglePlaceAdapter.OptionViewHolder>(), Filterable {
    private var listFiltered: ArrayList<GooglePlaceResponseModel.Prediction>? = dataList
    var forDestination: Boolean = false
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_google_place_view,
                p0,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        (holder).bind(listFiltered!!.get(position), position, listener)
    }

    override fun getItemCount(): Int {
        return listFiltered!!.size
    }

    fun updateAll(posts: List<GooglePlaceResponseModel.Prediction>) {
        this.listFiltered!!.clear()
        this.listFiltered!!.addAll(posts)
        notifyDataSetChanged()
    }

    fun addItem(posts: String) {
        //        this.slotsList.add(0, posts);
        //        notifyDataSetChanged();
    }


    inner class OptionViewHolder
        (view: View) : RecyclerView.ViewHolder(view) {
        @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun bind(
            part: GooglePlaceResponseModel.Prediction,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {
            //            menuBtnAdapter_front.bringToFront()

            try {
                placeName.setText(part.description.split(",")[0])
            } catch (e: Exception) {
                placeName.setText(part.description)
            }
            try {
                placeName2.setText(getOtherAddress(part.description))
            } catch (e: Exception) {
            }

            if (forDestination) {
                locationIcon.setImageResource(R.mipmap.ic_whereto)
            } else {
                locationIcon.setImageResource(R.mipmap.ic_your_location)
            }
            if (posit == (dataList.size - 1)) {
                footerImageView.visibility = View.VISIBLE
            } else {
                footerImageView.visibility = View.GONE
            }
//            Log.e("adapter ", " " + part.description)

            itemView.setOnClickListener {
                //                refreshView(posit)
                listener.onItemClick(itemView, posit, 0, listFiltered!!.get(posit))
            }
        }
    }

    private fun getOtherAddress(description: String): String {
        var addressDetail = StringBuilder()
        for (i in 0 until description.split(",").size) {
            if (i > 0) {
                Log.e(" " + i, "" + description.split(",")[i])
                addressDetail.append(description.split(",")[i]).append(", ")
            }
        }
        return AppUtils.removeLastCharOptional(addressDetail.toString())
    }


    /* private fun refreshView(posit: Int) {
         android.os.Handler().postDelayed({
             for (x in 0 until dataList.size)// println(x) // Prints 0 through 9
             {
                 if (posit == x) {
                     dataList.get(x).isSelected = true
                 } else dataList.get(x).isSelected = false
             }
             notifyDataSetChanged()

         }, 10)
     }*/

    fun getPositionData(position: Int): GooglePlaceResponseModel.Prediction? {

        return listFiltered!!.get(position)
    }


    override fun getFilter(): Filter {
        return object : Filter() {
            protected override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    listFiltered = dataList
                } else {
                    val filteredList = ArrayList<GooglePlaceResponseModel.Prediction>()
                    for (row in dataList) {
                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or description  match
                        if (row.description.toLowerCase().contains(charString.toLowerCase()) || row.description.contains(
                                charSequence
                            )
                        ) {
                            filteredList.add(row)
                        }
                    }
                    listFiltered = filteredList
                }

                val filterResults = FilterResults()
                filterResults.values = listFiltered
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                listFiltered =
                    filterResults.values as ArrayList<GooglePlaceResponseModel.Prediction>
                notifyDataSetChanged()
            }
        }
    }

    fun setSearchBoolean(forDestination: Boolean) {
        this.forDestination = forDestination;

    }
}

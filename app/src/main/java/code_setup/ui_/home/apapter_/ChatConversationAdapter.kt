package code_setup.ui_.home.apapter_

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import code_setup.app_core.BaseApplication
import code_setup.app_models.response_.LoginResponseModel
import code_setup.app_models.response_.MessageListResponseModel
import code_setup.app_models.response_.TourListResponseModel
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.app_util.location_utils.log
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.adapter_chat_conversation_view.view.*
import kotlinx.android.synthetic.main.my_chat_view.view.*
import kotlinx.android.synthetic.main.others_chat_view.view.*
import kotlin.math.log


class ChatConversationAdapter(
    internal var activity: FragmentActivity,
    val dataList: ArrayList<MessageListResponseModel.ResponseObj>,
    internal var listener: OnItemClickListener<Any>
) : androidx.recyclerview.widget.RecyclerView.Adapter<ChatConversationAdapter.OptionViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OptionViewHolder {
        return OptionViewHolder(
            LayoutInflater.from(activity).inflate(
                R.layout.adapter_chat_conversation_view,
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

    fun updateAll(posts: List<MessageListResponseModel.ResponseObj>) {
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
            part: MessageListResponseModel.ResponseObj,
            posit: Int,
            listener: OnItemClickListener<Any>
        ) = with(itemView) {

//            log(part.sender_id + " compair " + (BaseApplication.instance.getUserData() as LoginResponseModel.ResponseObj).driver_id)
            if (part.sender_id.equals((BaseApplication.instance.getUserData() as LoginResponseModel.ResponseObj).driver_id)) {
                myChatHolder.visibility = View.VISIBLE
                myChatText.setText(part.message)
                otherChatHolder.visibility = View.GONE
            } else {
                otherChatMessage.setText(part.message)
                otherChatHolder.visibility = View.VISIBLE
                myChatHolder.visibility = View.GONE
            }


            itemView.setOnClickListener {
                listener.onItemClick(
                    itemView,
                    posit,
                    CommonValues.TOUR_NAME_CLICKED,
                    dataList.get(posit)
                )
            }
        }
    }

}

/*
package code_setup.ui_.home.views.chat_

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import code_setup.app_core.BaseApplication
import code_setup.app_core.CoreActivity
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_models.response_.BaseResponseModel
import code_setup.app_models.response_.MessageListResponseModel
import code_setup.app_models.response_.OrderDetailResponseModel
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.callback_iface.OnItemClickListener
import code_setup.app_util.socket_work.SocketService
import code_setup.app_util.socket_work.model_.SocketUpdateModel
import code_setup.net_.NetworkCodes
import code_setup.ui_.home.apapter_.ChatConversationAdapter
import code_setup.ui_.home.di_home.HomeModule
import code_setup.ui_.home.home_mvp.HomePresenter
import code_setup.ui_.home.home_mvp.HomeView
import com.electrovese.setup.R
import com.google.gson.Gson
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.layout_chat_screen.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

class ChatScreen : CoreActivity(), HomeView {
    lateinit var adapterConvo: ChatConversationAdapter
    lateinit var riderDataMdl: OrderDetailResponseModel.ResponseObj.User
    lateinit var tourId: String

    @Inject
    lateinit var presenter: HomePresenter

    override fun onActivityInject() {
//        DaggerHomeComponent.builder().appComponent(getAppcomponent())
//            .homeModule(HomeModule())
//            .build()
//            .inject(this)
        presenter.attachView(this)
    }

    override fun getScreenUi(): Int {
        return R.layout.layout_chat_screen
    }

    override fun onResponse(list: Any, int: Int) {
        Log.e("onResponse", " " + Gson().toJson(list))
        when (int) {
            code_setup.net_.NetworkRequest.REQUEST_SEND_MESSAGE -> {
                var responseData = list as BaseResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    chatMessageField.setText("")
                    getConversation()
                }
            }
            code_setup.net_.NetworkRequest.REQUEST_FIND_CHAT_CONVERSATION -> {
                var responseData = list as MessageListResponseModel
                if (responseData.response_code == NetworkCodes.SUCCEES.nCodes) {
                    if (responseData.response_obj != null && responseData.response_obj.size > 0) {
                        adapterConvo.updateAll(responseData.response_obj)
                        chatRecyclar.smoothScrollToPosition((adapterConvo.itemCount - 1))
                    }
                }
            }
        }
    }

    override fun showProgress() {
        loaderViewChat.show()
    }

    override fun hideProgress() {
        loaderViewChat.hide()
    }


    override fun noResult() {

    }

    override fun onError() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        toolbarBack.setOnClickListener {
            onBackPressed()
        }
        getIntentData(intent)

        initAdapter()
        Handler().postDelayed(Runnable {
            showProgress()
            getConversation()
        }, 1000)
        val function: (View) -> Unit = {
            if (validated()) {

            }
        }
        sendMessageBtn.setOnClickListener(function)
    }

    override fun onResume() {
        super.onResume()
        subscribeForChat()
        BaseApplication.instance.CHAT_OPENED = true
    }

    override fun onPause() {
        super.onPause()
        BaseApplication.instance.CHAT_OPENED = false
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    fun onMessage(event: CustomEvent<Any>) {
        Log.d("onMessage", " CHAT SCREEN " + event.type)
        Log.d("onMessage", " CHAT SCREEN " + event.oj.toString())
        when (event.type) {
            EVENTS.NEW_MESSAGE -> {// In case of push notification
                getConversation()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun subscribeForChat() {
        try {
            if (SocketService.instance != null&&SocketService.instance.mSocket!!.connected()) {
                Prefs.putString(CommonValues.RECENT_SUBSCRIBER_ID, riderDataMdl.user_booking_id)
                SocketService.instance.mSocket!!.emit("subscribe", riderDataMdl.user_booking_id)
                SocketService.instance.mSocket!!.on(EVENTS.MESSAGE_RECEIVED, getMessage)
            } else {
                restartService()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            restartService()
        }
    }

    private fun restartService() {
        stopService(Intent(this, SocketService::class.java))
        Handler().postDelayed(Runnable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(Intent(this, SocketService::class.java))
            } else {
                startService(Intent(this, SocketService::class.java))
            }
            subscribeForChat()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (SocketService.instance != null) {
            SocketService.instance.mSocket!!.off(EVENTS.MESSAGE_RECEIVED)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val getMessage = Emitter.Listener { args ->

        Log.d("GET_MESSAGE_Socket: ", " 1 chat screen " + args[0])
        Log.d("GET_MESSAGE_Socket: ", " 2  chat screen " + Gson().toJson(args))

        var updatesModel = try {
            Gson().fromJson("" + args[0], SocketUpdateModel::class.java)
        } catch (e: Exception) {
            getConversation()
        }
//        Log.d(" GET GET GET ", "   " + Gson().toJson(updatesModel.message.data))
//        when (updatesModel.message.action_code) {
//            EVENTS.MESSAGE_RECEIVED -> {
//                getConversation()
//            }


//        }
        return@Listener

    }

    private fun validated(): Boolean {
        if (!chatMessageField.text.toString().isNotEmpty()) {
            AppUtils.showToast(getString(R.string.str_enter_text_message))
            return false
        }
        return true
    }

    private fun getConversation() {
    }

    private fun getIntentData(intent: Intent) {
        tourId = intent.getStringExtra(CommonValues.TOUR_ID)
        var dataValue = intent.getSerializableExtra(CommonValues.TOUR_DATA)
        riderDataMdl = dataValue as OrderDetailResponseModel.ResponseObj.User
        if (riderDataMdl != null) {
            txt_title_toolbar.setText(riderDataMdl.name)
        }
    }

    private fun initAdapter() {
        with(chatRecyclar) {
            adapterConvo = ChatConversationAdapter(this@ChatScreen,
                ArrayList(), object : OnItemClickListener<Any> {
                    override fun onItemClick(view: View, position: Int, type: Int, t: Any?) {

                    }
                })
            chatRecyclar.adapter = adapterConvo
            chatRecyclar.layoutManager = LinearLayoutManager(this@ChatScreen, VERTICAL, false)
        }
    }
}*/

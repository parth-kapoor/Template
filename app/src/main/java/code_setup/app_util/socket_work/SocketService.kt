package code_setup.app_util.socket_work

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.location_utils.log
import code_setup.app_util.socket_work.model_.SocketUpdateModel
import code_setup.net_.NetworkConstant
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.greenrobot.eventbus.EventBus


class SocketService : IntentService("SocketService") {
    companion object {
        var instance: SocketService = SocketService()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return Service.START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val CHANNEL_ID = "com.electrovese.get_atour_driver"
            val channel = NotificationChannel(
                CHANNEL_ID, "Geta Tour", NotificationManager.IMPORTANCE_DEFAULT
            )
            (getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).createNotificationChannel(
                channel)

            val notification: Notification = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle("Geta Tour")
                .setContentText("").build()
            startForeground(1, notification)
        }

        connectToScoket()
    }

    private var isConnected: Boolean = false
    var mSocket: Socket? = null
    private val TAG = SocketService::class.java.simpleName

    fun connectToScoket() {
        try {
            val opts = IO.Options()
            opts.forceNew = true
            opts.query = "token=" + Prefs.getString(CommonValues.ACCESS_TOKEN, "")
            mSocket = IO.socket(removeLastChar(NetworkConstant.BASE_URL_SOCKET), opts)
//                mSocket = IO.socket("http://91.205.173.97:6161", opts)
            mSocket!!.connected()
//            mSocket!!.on(EVENTS.NEW_TOUR_JOB, getMessage)



            mSocket!!.on(Socket.EVENT_CONNECT, onConnect);
            mSocket!!.on(Socket.EVENT_DISCONNECT, onDisconnect);
            mSocket!!.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            mSocket!!.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//            mSocket!!.on(Socket.EVENT_ERROR, onError);


            mSocket!!.connect()

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    fun removeLastChar(str: String): String {
        return str.substring(0, str.length - 1)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val getMessage = Emitter.Listener { args ->

        Log.d("SocketService : ", " 1  " + args[0])
//        Log.d("GET_MESSAGE_Socket: ", " 2  " + Gson().toJson(args))

        var updatesModel = Gson().fromJson("" + args[0], SocketUpdateModel::class.java)
        Log.d("SocketService : ", "   " + Gson().toJson(updatesModel.message.data))
        when (updatesModel.message.action_code) {
//            EVENTS.NEW_TOUR_JOB -> {
//                EventBus.getDefault()
//                    .postSticky(CustomEvent<Any>(EVENTS.SOCKET_REQUEST_NEW_TOUR_JOB, updatesModel))
//            }

        }
        return@Listener

    }
    private val onConnect =
        Emitter.Listener {
            isConnected = true
            log("SOCKET@  Connected...")
            isConnected = true
        }
    private val onDisconnect =
        Emitter.Listener {
            isConnected = false

            log("SOCKET@ Disconnected....")
//            connectToScoket()
            isConnected = false
        }
    private val onConnectError =
        Emitter.Listener {
            isConnected = false

            log("SOCKET@ Failed to connect...")
//            disconnectToSercer()

//            connectToScoket()
            isConnected = false
        }
    private val onError =
        Emitter.Listener { }

    override fun onStart(intent: Intent?, startId: Int) {
        super.onStart(intent, startId)
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onHandleIntent(p0: Intent?) {

    }


    override fun onDestroy() {
        super.onDestroy()
        disconnectToSercer()
    }

    private fun disconnectToSercer() {
        try {
            if (mSocket!!.connected()) {
                mSocket!!.emit(
                    "unsubscribe",
                    Prefs.getString(CommonValues.RECENT_SUBSCRIBER_ID, "")
                )
                mSocket!!.off(Socket.EVENT_CONNECT, onConnect);
                mSocket!!.off(Socket.EVENT_DISCONNECT, onDisconnect);
                mSocket!!.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
                mSocket!!.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
//                mSocket!!.off(Socket.EVENT_ERROR, onError);
                mSocket!!.disconnect()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

package code_setup.app_util.broadcasr_receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.util.Log
import code_setup.app_core.BaseApplication.Companion.recentNetworkStatus
import code_setup.app_core.CoreActivity
import code_setup.app_models.other_.event.CustomEvent
import code_setup.app_models.other_.event.EVENTS
import org.greenrobot.eventbus.EventBus

class NetworkChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            if (isOnline(context)) { //
                if (!recentNetworkStatus) {
                    EventBus.getDefault()
                        .postSticky(CustomEvent<Any>(EVENTS.NETWORK_CONNECTION_CALLBACK, true))
                    recentNetworkStatus = true
                }
                Log.e("NetworkChangeReceiver", "Online Connect Intenet ")
            } else {
                if (recentNetworkStatus) {
                    EventBus.getDefault().postSticky(CustomEvent<Any>(EVENTS.NETWORK_CONNECTION_CALLBACK, false))
                    recentNetworkStatus = false
                }
                Log.e("NetworkChangeReceiver", "Conectivity Failure !!! ")
            }
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun isOnline(context: Context): Boolean {
        return try {
            val cm =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = cm.activeNetworkInfo
            //should check null because in airplane mode it will be null
            netInfo != null && netInfo.isConnected
        } catch (e: NullPointerException) {
            e.printStackTrace()
            false
        }
    }
}
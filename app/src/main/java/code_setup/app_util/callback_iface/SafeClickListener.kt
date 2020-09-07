package code_setup.app_util.callback_iface

import android.os.SystemClock
import android.view.View
/**
 * Created by arischoice on 20/1/2019.
 *
 * workd like debouncing onclick at a time
 */

class SafeClickListener(
    private var defaultInterval: Int = 1000,
    private val onSafeCLick: (View) -> Unit
) : View.OnClickListener {
    private var lastTimeClicked: Long = 0
    override fun onClick(v: View) {
        if (SystemClock.elapsedRealtime() - lastTimeClicked < defaultInterval) {
            return
        }
        lastTimeClicked = SystemClock.elapsedRealtime()
        onSafeCLick(v)
    }


}
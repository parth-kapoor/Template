package code_setup.app_util.callback_iface

import android.view.View

/**
 * Created by arischoice on 20/1/2019.
 */

interface OnItemClickListener<T> {

    fun onItemClick(view: View, position: Int, type: Int, t: Any?)

}
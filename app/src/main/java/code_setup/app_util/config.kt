package code_setup.app_util

import android.content.Context
import android.os.Build
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import com.electrovese.setup.R
import com.google.android.material.snackbar.Snackbar

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Snackbar.config(context: Context) {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params

    this.view.background = context.getDrawable(R.drawable.bg_snackbar)

    ViewCompat.setElevation(this.view, 6f)
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Snackbar.configForNetwork(context: Context, status: Boolean) {
    val params = this.view.layoutParams as ViewGroup.MarginLayoutParams
    params.setMargins(12, 12, 12, 12)
    this.view.layoutParams = params
    if (status)
        this.view.background = context.getDrawable(R.drawable.bg_snackbar_appcolor)
    else{
        this.view.background = context.getDrawable(R.drawable.bg_snackbar_red)
    }

    ViewCompat.setElevation(this.view, 6f)
}
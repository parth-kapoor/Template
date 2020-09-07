package code_setup.app_core

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import code_setup.app_util.di.AppComponent
import com.electrovese.setup.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

/**
 * Created by arischoice on 20/1/2019.
 */
abstract class CoreBottomSheetFragment() : BottomSheetDialogFragment() {


    /**
     * Activity  intents with bundel
     */
    fun activitySwitcher(from: FragmentActivity, to: Class<*>, bundle: Bundle?) {

        val intent = Intent(from, to)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        from.startActivity(intent)
        from.overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Log.e("onRequestPermissionsResult :: ", "User permissions granted" + "")
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        onActivityInject()
    }

    /**
     * attach dagger with activity
     */
    protected abstract fun onActivityInject()
    fun getAppcomponent(): AppComponent = BaseApplication.appComponent
}
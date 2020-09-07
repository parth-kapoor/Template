package code_setup.app_util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import code_setup.app_core.BaseApplication
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import code_setup.app_models.other_.CabsDataModel
import code_setup.app_models.response_.CaptureInfoModel
import code_setup.app_util.location_utils.log
import code_setup.ui_.settings.views.TermsConditionsActivity
import com.electrovese.setup.BuildConfig
import com.electrovese.setup.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import java.util.*


class AppUtils {
    companion object {


        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun showSnackBar(context: Activity, stringMessage: String) {
            val snack: Snackbar = Snackbar.make(
                context.findViewById(android.R.id.content),
                stringMessage,
                Snackbar.LENGTH_LONG
            )
//            SnackbarHelper.configSnackbar(this, snack);
            snack.config(context = context)// if you're using Kotlin
            snack.show()
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        fun showSnackBarConnectiivity(context: Activity, stringMessage: String, staus: Boolean) {
            var snack: Snackbar
//            SnackbarHelper.configSnackbar(this, snack);
            if (!staus) {
                snack = Snackbar.make(
                    context.findViewById(android.R.id.content),
                    stringMessage,
                    Snackbar.LENGTH_INDEFINITE
                )
            } else {
                snack = Snackbar.make(
                    context.findViewById(android.R.id.content),
                    stringMessage,
                    Snackbar.LENGTH_SHORT
                )
            }
            snack.configForNetwork(context = context, status = staus)// if you're using Kotlin
            snack.show()
        }

        fun isValidEmail(email: String): Boolean {
            return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }


        fun showToast(stringMsg: String) {
            Toast.makeText(BaseApplication.instance, stringMsg, Toast.LENGTH_SHORT).show()
        }

        fun spannableTextLink(ctx: Context, viewT: TextView, text: String) {
//        val termConditionTxt = getClickableSpan(R.color.colorAccent, null, termConditionTxt)


            var sText: SpannableString = SpannableString(text)
            var clickableSpan = getClickableSpan(sText, ctx)
            sText.setSpan(
                clickableSpan,
                text.length - 11,
                text.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            viewT.setText(sText)
            viewT.setMovementMethod(LinkMovementMethod.getInstance())
            viewT.setHighlightColor(Color.BLACK)

        }

        fun getClickableSpan(sText: SpannableString, ctx: Context): ClickableSpan {
            return object : ClickableSpan() {
                override fun onClick(view: View) {
                    try {
                        ctx.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(CommonValues.APPLICATION_PLAYSTORE_URL_CUSTOMER)
                            )
                        );
                    } catch (e: android.content.ActivityNotFoundException) {
                        ctx.startActivity(
                            Intent(
                                Intent.ACTION_VIEW,
                                Uri.parse(CommonValues.APPLICATION_PLAYSTORE_URL_CUSTOMER)
                            )
                        );
                    }
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setUnderlineText(false) // set to false to remove underline
                    ds.color = Color.parseColor("#F8345E")
                }
            }
        }

        fun getCaptureInfoData(): CaptureInfoModel {
            return CaptureInfoModel(
                AppUtils.getDeviceid(),
                CommonValues.DEVICE_OS,
                BuildConfig.VERSION_NAME,
                Build.VERSION.RELEASE,
                Prefs.getString(CommonValues.FCM_TOKEN, "")
            )
        }

        @SuppressLint("MissingPermission")
        fun getDeviceid(): String {
            var androidId = Prefs.getString(CommonValues.DEVICE_ID, "fgj")
            Log.d("ANDROID ID ", " == " + androidId)
            return androidId!!
        }

        fun getTimeZoneWithOffset(): String {

            val tz = TimeZone.getDefault()
            val now = Date();
//Import part : x.0 for double number
            val offsetFromUtc = tz.getOffset(now.getTime()) / 3600000.0
            val m2tTimeZoneIs = offsetFromUtc.toString()

            return m2tTimeZoneIs
        }

        fun getMyLocation(): LatLng? {
            return LatLng(
                Prefs.getDouble(CommonValues.LATITUDE, 0.0),
                Prefs.getDouble(CommonValues.LONGITUDE, 0.0)
            )
        }
        /*
        * capitalize first char
        *  */

        fun capWorldString(str: String): String {
            var strArray = str.length
            var builder = StringBuilder();

            for (i in 0 until strArray) {
                if (i == 0) {
                    var cap = str.subSequence(0, 1).toString().toUpperCase()
                    builder.append("$cap");
                } else {
                    var cap = str.subSequence(i, i + 1).toString().toLowerCase()
                    builder.append("$cap");
                }
            }
            return builder.toString()
        }


        /*
        * Remove notification from id
        * */
        fun clearNotification(ctx: Activity, notificationId: Int) {
            var notificationManager =
                ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancel(notificationId);
        }


        /*
     **********Update STATUS BAR ICONS COLOR**********
      * */

        fun statusBarIconTheme(ctx: Activity, shouldChangeStatusBarTintToDark: Boolean) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                var decor = ctx.getWindow().getDecorView();
                if (shouldChangeStatusBarTintToDark) {
                    decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                } else {
                    // We want to change tint color to white again.
                    // You can also record the flags in advance so that you can turn UI back completely if
                    // you have set other flags before, such as translucent or full screen.
                    decor.setSystemUiVisibility(0);
                }
            }
        }

        /*
            **********Update STATUS BAR  COLOR**********
             * */
        fun changeStatusBarColor(
            ctx: Activity,
            color: Int,
            shouldChangeStatusBarTintToDark: Boolean
        ) {
            if (Build.VERSION.SDK_INT >= 21) {
                var window = ctx.getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.setStatusBarColor(ctx.getResources().getColor(color));
                statusBarIconTheme(ctx, shouldChangeStatusBarTintToDark)
            }
        }

        fun handleonScreenBottomNavigation(contxt: Activity) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                /*  getWindow().setFlags(
                      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                      WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                  )*/
                contxt.window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                )

                contxt.getWindow().getDecorView()
                    .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR)

            }
        }

        fun hideKeyboard(loginButton: View?) {
            val imm =
                BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(loginButton!!.windowToken, 0)
        }

        public fun showKeyboard(vw: View?) {
            vw!!.requestFocus()
            val inputMethodManager =
                BaseApplication.instance.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(vw, InputMethodManager.SHOW_FORCED)
        }

        fun spannableTextOtpScreen(viewT: TextView, text: String) {
            val textString = "Enter 4 digits OTP code  sent to your mobile number " + text
            var sText: SpannableString = SpannableString(textString)
            var clickableSpan = getClickable(sText)
            sText.setSpan(clickableSpan, 52, textString.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewT.setText(sText)
            viewT.setMovementMethod(LinkMovementMethod.getInstance())
            viewT.setHighlightColor(Color.TRANSPARENT)

        }

        fun getClickable(sText: SpannableString): ClickableSpan {
            return object : ClickableSpan() {
                override fun onClick(view: View) {
                    log("onClick")
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setUnderlineText(false) // set to false to remove underline
                    ds.color = Color.parseColor("#000000")
                }
            }
        }


        fun spannableTextTerms(
            viewT: TextView,
            text: String,
            acticity: Activity
        ) {
            var sText: SpannableString =
                SpannableString("I agree to the Privacy Policy and Terms and Conditions")
            var clickableSpan = getClickableSpan(sText, acticity)
            var clickableSpan1 = getClickableSpan1(sText, acticity)
            sText.setSpan(clickableSpan, 15, 29, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sText.setSpan(clickableSpan1, 34, sText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            viewT.setText(sText)
            viewT.setMovementMethod(LinkMovementMethod.getInstance())
            viewT.setHighlightColor(Color.TRANSPARENT)

        }

        fun getClickableSpan(sText: SpannableString, acticity: Activity): ClickableSpan {
            return object : ClickableSpan() {
                override fun onClick(view: View) {
                    Log.d("onClick   ", "" + sText)
                    var intent = Intent(acticity, TermsConditionsActivity::class.java)
                    intent.putExtra(CommonValues.SCREEN_TYPE, CommonValues.POLICY)
                    acticity.startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setUnderlineText(false) // set to false to remove underline
                    ds.color = Color.parseColor("#F8345E")
                }
            }
        }

        fun getClickableSpan1(sText: SpannableString, acticity: Activity): ClickableSpan {
            return object : ClickableSpan() {
                override fun onClick(view: View) {
                    Log.d("onClick1   ", "" + sText)
                    var intent = Intent(acticity, TermsConditionsActivity::class.java)
                    intent.putExtra(CommonValues.SCREEN_TYPE, CommonValues.TERMS)
                    acticity.startActivity(intent)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.setUnderlineText(false) // set to false to remove underline
                    ds.color = Color.parseColor("#F8345E")
                }
            }
        }

        /*
        * DEFAULT cab list provider
        * */
        fun getCabsList(activity: Activity?): ArrayList<CabsDataModel> {
            var cabList = ArrayList<CabsDataModel>()
            cabList.add(CabsDataModel(R.mipmap.ic_hatchback, "Hatchback/Sedan", "₹ 250", true))
            cabList.add(CabsDataModel(R.mipmap.ic_muv, "XUV/MUV", "₹ 350", false))
            return cabList
        }

        fun removeLastCharOptional(str: String): String {
            return str.substring(0, str.length - 2);
        }
    }

}

package code_setup.ui_.auth.views.authantication_

import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.transition.ChangeBounds
import android.transition.Slide
import android.transition.Transition
import android.view.Gravity.LEFT
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import com.electrovese.setup.R
import kotlinx.android.synthetic.main.activity_login_with_phone.*

class LoginWithPhone : AppCompatActivity() {


    internal var enterTransitionListener: Transition.TransitionListener =
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                ivback!!.imageAlpha = 0
            }

            override fun onTransitionEnd(transition: Transition) {
                ivback!!.imageAlpha = 255
                val animation =
                    AnimationUtils.loadAnimation(this@LoginWithPhone, R.anim.slide_right)
                ivback!!.startAnimation(animation)

            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        }


    internal var returnTransitionListener: Transition.TransitionListener =
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

                val imm = getSystemService(
                    Context.INPUT_METHOD_SERVICE
                ) as InputMethodManager
                imm.hideSoftInputFromWindow(etPhoneNo!!.windowToken, 0)
                tvMoving!!.text = null
                tvMoving!!.hint = getString(R.string.enter_your_phone_number)
//            ivFlag!!.alpha = 0f
                tvCode!!.alpha = 0f
                holderCode!!.alpha = 0f
                etPhoneNo!!.visibility = View.GONE
                etPhoneNo!!.isCursorVisible = false
                etPhoneNo!!.background = null
                val animation = AnimationUtils.loadAnimation(this@LoginWithPhone, R.anim.slide_left)
                ivback!!.startAnimation(animation)
            }

            override fun onTransitionEnd(transition: Transition) {


            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        }

    internal var exitTransitionListener: Transition.TransitionListener =
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {

                rootFrame!!.alpha = 1f
//            fabProgressCircle!!.hide()
                llphone!!.setBackgroundColor(Color.parseColor("#EFEFEF"))
            }

            override fun onTransitionEnd(transition: Transition) {


            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        }


    internal var reenterTransitionListener: Transition.TransitionListener =
        @RequiresApi(Build.VERSION_CODES.KITKAT)
        object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {


            }

            override fun onTransitionEnd(transition: Transition) {

                llphone!!.setBackgroundColor(Color.parseColor("#FFFFFF"))
                etPhoneNo!!.isCursorVisible = true
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)

            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }
        }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_with_phone)
        setupWindowAnimations()
        ivback.setOnClickListener {
            onBackPressed()
            AppUtils.hideKeyboard(ivback)

        }
        floatBtn.setOnClickListener {
            if (validated()) {
                AppUtils.hideKeyboard(floatBtn)
                val intent = Intent(this@LoginWithPhone, PasswordActivity::class.java)
                intent.putExtra(CommonValues.COUNTRY_CODE, tvCode.selectedCountryCode)
                intent.putExtra(CommonValues.PHONE_NUMBER, etPhoneNo.text.toString())

                if(getIntent().getBooleanExtra(CommonValues.IS_SOCIAL_LOGIN,false)){
                    intent.putExtra(CommonValues.SOCIAL_DATA_USER, getIntent().getSerializableExtra(CommonValues.SOCIAL_DATA_USER))
                    intent.putExtra(CommonValues.IS_SOCIAL_LOGIN, true)
                }


                val options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this@LoginWithPhone)
                startActivity(intent, options.toBundle())
            }
        }
        Handler().postDelayed(Runnable {
            AppUtils.showKeyboard(etPhoneNo)
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        }, 1000)
    }

    private fun validated(): Boolean {
        if (!etPhoneNo.text.toString().isNotEmpty()) {
            AppUtils.showSnackBar(this, getString(R.string.error_enter_Valid_mobile_number))
            return false
        } else if (etPhoneNo.text.toString().length <= 9) {
            AppUtils.showSnackBar(this, getString(R.string.error_enter_Valid_mobile_number))
            return false
        }

        return true
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setupWindowAnimations() {

        val enterTransition = ChangeBounds()
        enterTransition.duration = 700
        enterTransition.interpolator = DecelerateInterpolator(4f)
        enterTransition.addListener(enterTransitionListener)
        window.sharedElementEnterTransition = enterTransition

        val returnTransition = ChangeBounds()
        returnTransition.duration = 700
        returnTransition.addListener(returnTransitionListener)
        window.sharedElementReturnTransition = returnTransition

        val exitSlide = Slide(LEFT)
        exitSlide.duration = 700
        exitSlide.addListener(exitTransitionListener)
        exitSlide.addTarget(R.id.llphone)
        exitSlide.interpolator = DecelerateInterpolator()
        window.exitTransition = exitSlide

        val reenterSlide = Slide(LEFT)
        reenterSlide.duration = 700
        reenterSlide.addListener(reenterTransitionListener)
        reenterSlide.interpolator = DecelerateInterpolator(2f)
        reenterSlide.addTarget(R.id.llphone)
        window.reenterTransition = reenterSlide

    }
}

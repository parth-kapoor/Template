package code_setup.ui_.auth.views.authantication_

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.Transition
import android.transition.Transition.TransitionListener
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.viewpager.widget.ViewPager
import code_setup.app_core.BaseApplication
import code_setup.app_util.AppUtils
import code_setup.app_util.CommonValues
import code_setup.app_util.Prefs
import code_setup.app_util.location_utils.log
import code_setup.net_.NetworkConstant
import code_setup.ui_.auth.adapter.OnboardingAdapter
import code_setup.ui_.auth.models.request_model.RequestSocialLogin
import code_setup.ui_.auth.models.response_model.StoreUserResponseModel
import code_setup.ui_.home.views.HomeActivity
import com.electrovese.setup.R
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login_new.*
import kotlinx.android.synthetic.main.login_top_view_text.*
import org.json.JSONException
import org.json.JSONObject
import java.util.*


class LoginActNew : AppCompatActivity() {


    lateinit var fbUserData: RequestSocialLogin
    lateinit var callbackManager: CallbackManager
    internal var exitListener: TransitionListener = @RequiresApi(Build.VERSION_CODES.KITKAT)
    object : TransitionListener {
        override fun onTransitionStart(transition: Transition) {


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


    internal var reenterListener: TransitionListener = @RequiresApi(Build.VERSION_CODES.KITKAT)
    object : TransitionListener {
        override fun onTransitionStart(transition: Transition) {

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(ObjectAnimator.ofFloat(tvMoving, "alpha", 0f, 1f))
            animatorSet.playTogether(ObjectAnimator.ofFloat(tvMoving1, "alpha", 0f, 1f))
            animatorSet.duration = 800
            animatorSet.start()
        }

        override fun onTransitionEnd(transition: Transition) {


        }

        override fun onTransitionCancel(transition: Transition) {

        }

        override fun onTransitionPause(transition: Transition) {

        }

        override fun onTransitionResume(transition: Transition) {

            tvMoving.setAlpha(1f)
            tvMoving1.setAlpha(1f)
        }
    }


    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_new)

        setupWindowAnimations()

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        val height = displayMetrics.heightPixels
        ivUberLogo.setLayoutParams(
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (0.58 * height).toInt()
            )
        )
        ivback.setImageAlpha(0)

        tvPhoneNo.setOnClickListener {
            startTransition()
        }
        BaseApplication.instance.generateFirebaseToken(this)
//        initFbCallback()

        fbTV.setOnClickListener {
            //            startActivity(Intent(this, HomeActivity::class.java))
            AppUtils.showSnackBar(this, "Working on this feature")
//            LoginManager.getInstance()
//                .logInWithReadPermissions(this, Arrays.asList("public_profile"));
        }
        initPager()
    }

    override fun onResume() {
        super.onResume()
        AppUtils.hideKeyboard(tvPhoneNo)
    }

    private fun initFbCallback() {
        LoginManager.getInstance().logOut();
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
            object : FacebookCallback<LoginResult?> {
                override fun onSuccess(loginResult: LoginResult?) { // App code
//                    Log.e("Fb ", " onSuccess " + Gson().toJson(loginResult));
                    getProfileData(loginResult)
//                    requestLoginProcess(loginResult)
                }

                override fun onCancel() { // App code
                    AppUtils.showSnackBar(this@LoginActNew, "Login Canceled")
                }

                override fun onError(exception: FacebookException) { // App code
                    AppUtils.showSnackBar(this@LoginActNew, "Login Error")
                }
            })
    }

    private fun getProfileData(loginResult: LoginResult?) {
        val newMeRequest = GraphRequest.newMeRequest(
            loginResult!!.getAccessToken(),
            object : GraphRequest.GraphJSONObjectCallback {
                override fun onCompleted(jsnObject: JSONObject, response: GraphResponse) {
                    // Application code
                    try {
                        Log.i("Response", jsnObject.toString());

                        val firstName = jsnObject.getString("first_name");
                        val lastName = jsnObject.getString("last_name");
                        val idStr = jsnObject.getString("id");
                        val email = try {
                            jsnObject.getString("email");
                        } catch (e: Exception) {
                            ""
                        }

//                    val gender = response.getJSONObject().getString("gender");


                        /* val profile = Profile.getCurrentProfile();
                         String id = profile.getId();
                         String link = profile.getLinkUri().toString();
                         Log.i("Link",link);
                         if (Profile.getCurrentProfile()!=null)
                         {
                             Log.i("Login", "ProfilePic" + Profile.getCurrentProfile().getProfilePictureUri(200, 200));
                         }*/

                        Log.i("Login" + "Email", email);
                        Log.i("Login" + "FirstName", firstName);
                        Log.i("Login" + "LastName", lastName);
                        requestLoginProcess(
                            RequestSocialLogin(
                                "$firstName $lastName",
                                email,
                                idStr,
                                "FACEBOOK"
                            )
                        )

                    } catch (e: JSONException) {
                        e.printStackTrace();
                    }
                }
            })
        val request = newMeRequest;
        val parameters = Bundle();
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }


    var mCompositeDisposable: CompositeDisposable? = null
    private fun requestLoginProcess(loginResult: RequestSocialLogin) {
        fbUserData = loginResult

        mCompositeDisposable = CompositeDisposable()
        val apiService = RestConfig.create()
        mCompositeDisposable?.add(
            apiService.requestSocialLogin(
                BaseApplication.instance.getCommonHeaders(), loginResult
            )
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleSuccess, this::handleFaliur)
        )
    }

    fun handleSuccess(baseResponse: StoreUserResponseModel) {
        var bResponse = baseResponse
        if (bResponse.response_code == NetworkConstant.SUCCESS) {
            log("handleSuccess if " + "  ")

            Prefs.putBoolean(CommonValues.IS_LOGEDIN, true)
            Prefs.putString(CommonValues.ACCESS_TOKEN, bResponse.response_obj.token)
            Prefs.putString(
                CommonValues.USER_DATA,
                Gson().toJson(bResponse.response_obj)
            )
            startActivity(Intent(this, HomeActivity::class.java))
            finishAffinity()

        } else if (bResponse.response_code == NetworkConstant.FAIL) {

            val bndle = Intent(this, LoginWithPhone::class.java)
            bndle.putExtra(CommonValues.SOCIAL_DATA_USER, fbUserData)
            bndle.putExtra(CommonValues.IS_SOCIAL_LOGIN, true)
            startActivity(bndle)
        } else {

        }
    }

    fun handleFaliur(error: Throwable) {
        log("handleFaliur " + "   LOCATION UPDATE ")
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun setupWindowAnimations() {

        val exitTransition = ChangeBounds()
        exitTransition.duration = 700
        exitTransition.addListener(exitListener)
        window.sharedElementExitTransition = exitTransition

        val reenterTransition = ChangeBounds()
        reenterTransition.duration = 700
        reenterTransition.addListener(reenterListener)
        reenterTransition.interpolator = DecelerateInterpolator(4f)
        window.sharedElementReenterTransition = reenterTransition
    }

    private lateinit var adapterViewPager: OnboardingAdapter
    private fun initPager() {
        adapterViewPager = OnboardingAdapter(getSupportFragmentManager())
        introPager.setAdapter(adapterViewPager)
        pagerIndicator.setViewPager(introPager)

        // Attach the page change listener inside the activity
        // Attach the page change listener inside the activity
        introPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            // This method will be invoked when a new page becomes selected.
            override fun onPageSelected(position: Int) {
//                Toast.makeText(this@OnboardingActivity,
//                        "Selected page position: $position", Toast.LENGTH_SHORT).show()
                /* if (position == 4) {
                     nextBtn.visibility = View.VISIBLE
                     nextBtn.setText(R.string.str_login)
                 } else {
                     nextBtn.visibility = View.VISIBLE
                     nextBtn.setText(R.string.skip)
                 }*/

            }

            // This method will be invoked when the current page is scrolled
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) { // Code goes here
            }

            // Called when the scroll state changes:
// SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            override fun onPageScrollStateChanged(state: Int) { // Code goes here
            }
        })
    }

    //    @OnClick(R.id.llphone, R.id.ivFlag, R.id.tvPhoneNo)
    internal fun startTransition() {

        val intent = Intent(this@LoginActNew, LoginWithPhone::class.java)
//        val p1 = androidx.core.util.Pair.create(ivback as View, getString(R.string.transition_arrow))
//        val p2 = androidx.core.util.Pair.create(ivFlag as View, getString(R.string.transition_ivFlag))
        val p3 =
            androidx.core.util.Pair.create(tvCode as View, getString(R.string.transition_tvCode))
        val p4 = androidx.core.util.Pair.create(
            tvPhoneNo as View,
            getString(R.string.transition_tvPhoneNo)
        )
        val p5 =
            androidx.core.util.Pair.create(llphone as View, getString(R.string.transition_llPhone))
        val p6 = androidx.core.util.Pair.create(
            holderCode as View,
            getString(R.string.transition_tvCode)
        )
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, p3, p4, p5, p6)
        startActivity(intent, options.toBundle())


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data)
    }
}

package code_setup.app_util

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.transition.TransitionManager
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.Animation.AnimationListener
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import code_setup.app_util.test_utils.ResizeAnimation


class AnimUtils {
    companion object {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        internal fun startRevealAnimation(viewRoott: FrameLayout, viewChild: View) {
            try {
                val cx = viewRoott.getMeasuredWidth() / 2
                val cy = viewRoott.getMeasuredHeight() / 2

                val anim =
                    ViewAnimationUtils.createCircularReveal(
                        viewChild,
                        cx,
                        cy,
                        50f,
                        viewChild.getWidth().toFloat()
                    )

                anim.duration = 500
                anim.interpolator = AccelerateInterpolator(2f)
                anim.addListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                    }

                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)

                        TransitionManager.beginDelayedTransition(viewRoott)
                    }
                })
                anim.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }


        /**
         * move icon in y  direction
         */
        public fun moveAnimationY(imageCab: View?, b: Boolean,duration:Long) {
            // Move the object at the X position 50
            if (b) {
                val anim = ObjectAnimator.ofFloat(imageCab, View.TRANSLATION_Y, -0f)
                anim.setDuration(duration) // duration 2 seconds
                anim.start()
            } else {
                val anim = ObjectAnimator.ofFloat(imageCab, View.TRANSLATION_Y, 500f)
                anim.setDuration(duration) // duration 0 seconds
                anim.start()
            }

        }

        /**
         * move icon in y  direction
         */
        fun moveAnimationX(imageCab: View?, b: Boolean) {
            // Move the object at the X position 50
            if (b) {
                val anim = ObjectAnimator.ofFloat(imageCab, View.TRANSLATION_X, 10f)
                anim.setDuration(1000) // duration 2 seconds
                anim.start()
            } else {
                val anim = ObjectAnimator.ofFloat(imageCab, View.TRANSLATION_X, -100f)
                anim.setDuration(0) // duration 0 seconds
                anim.start()
            }

        }

        /**
         * move icon in circular rotation
         */
        fun rotateViewAnimatio(view: View) {
            val rotate = RotateAnimation(
                0f, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f
            )

            rotate.duration = 20000
            rotate.repeatCount = 10
            view.setAnimation(rotate)
        }

        /**
         * move icon to left
         */
        fun homeAnimationX(iconM: View?, b: Boolean) {
            val anim = ObjectAnimator.ofFloat(iconM, View.TRANSLATION_X, -50f)
            anim.setDuration(0) // duration 0 seconds
            anim.start()
        }

        /**
         * move icon to right
         */
        fun homeAnimationX1(iconM: View?, b: Boolean) {
            val anim = ObjectAnimator.ofFloat(iconM, View.TRANSLATION_X, 0f)
            anim.setDuration(0) // duration 0 seconds
            anim.start()
        }

        /**
         * resize view
         */
        fun resizeView(textView: View, i: Long, intStart: Int, intTarget: Int) {
            val resizeAnimation = ResizeAnimation(
                textView,
                intTarget,
                intStart
            )
            resizeAnimation.duration = 1000
            textView.startAnimation(resizeAnimation)
        }


        fun SlideToAbove(viewRoot: View) {
            var slide: Animation? = null
            slide = TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                -5.0f
            )
            slide.setDuration(400)
            slide.setFillAfter(true)
            slide.setFillEnabled(true)
            viewRoot.startAnimation(slide)
            slide.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    viewRoot.clearAnimation()
                    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        viewRoot.getWidth(), viewRoot.getHeight()
                    )
                    // lp.setMargins(0, 0, 0, 0);
//                    lp.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                    viewRoot.setLayoutParams(lp)
                }
            })
        }


        fun SlideToDown(viewRoot: View) {
            var slide: Animation? = null
            slide = TranslateAnimation(
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                0.0f,
                Animation.RELATIVE_TO_SELF,
                5.2f
            )
            slide.setDuration(400)
            slide.setFillAfter(true)
            slide.setFillEnabled(true)
            viewRoot.startAnimation(slide)
            slide.setAnimationListener(object : AnimationListener {
                override fun onAnimationStart(animation: Animation) {}
                override fun onAnimationRepeat(animation: Animation) {}
                override fun onAnimationEnd(animation: Animation) {
                    viewRoot.clearAnimation()
                    val lp: LinearLayout.LayoutParams = LinearLayout.LayoutParams(
                        viewRoot.getWidth(), viewRoot.getHeight()
                    )
                    lp.setMargins(0, viewRoot.getWidth(), 0, 0)
//                    lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                    viewRoot.setLayoutParams(lp)
                }
            })
        }

        fun moveAnimationXinFromRight(view: View?, valueX: Float, duration: Long) {
            val anim = ObjectAnimator.ofFloat(view, View.TRANSLATION_X, valueX)
            anim.setDuration(duration) // duration 0 seconds
            anim.start()
        }

        fun moveAnimationY_up_down(view: LinearLayout?, valueY: Float, duration: Long) {
            val anim = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, valueY)
            anim.setDuration(duration) // duration 0 seconds
            anim.start()
        }


    }
}

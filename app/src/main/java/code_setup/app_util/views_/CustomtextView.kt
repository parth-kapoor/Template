package code_setup.app_util.views_

import android.content.Context
import android.graphics.Typeface
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View
import code_setup.app_util.FontCache
import code_setup.app_util.callback_iface.SafeClickListener
/**
 * Created by arischoice on 20/1/2019.
 */
internal class CustomtextView : AppCompatTextView {

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        applyCustomFont(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        applyCustomFont(context, attrs)
    }

    private fun applyCustomFont(context: Context, attrs: AttributeSet) {
        val textStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL)
        val customFont = selectTypeface(context, textStyle)
        typeface = customFont
    }


    private fun selectTypeface(context: Context, textStyle: Int): Typeface? {

        when (textStyle) {
            Typeface.BOLD -> return FontCache.getTypeface(
                MONTSERRAT_BOLD,
                context
            )

            Typeface.ITALIC -> return FontCache.getTypeface(
                MONTSERRAT_REGULAR,
                context
            )

            Typeface.NORMAL -> return FontCache.getTypeface(
                MONTSERRAT_REGULAR,
                context
            )
            else -> return FontCache.getTypeface(
                MONTSERRAT_REGULAR,
                context
            )
        }
    }

    fun setSafeOnClickListener(onSafeClick: (View) -> Unit) {
        val safeClickListener = SafeClickListener {
            onSafeClick(it)
        }
        setOnClickListener(safeClickListener)
    }

    companion object {
        val SAMPL_FONT = "fonts/HURTM___"
        val MONTSERRAT_BOLD =SAMPL_FONT //"fonts/Montserrat-Bold"
        val MONTSERRAT_REGULAR = SAMPL_FONT//"fonts/Montserrat-Regular.ttf"

        val ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android"
    }


}
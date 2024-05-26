package com.jimbonlemu.whacchudoin.components

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.jimbonlemu.whacchudoin.R

class PasswordTextFormField @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
    AppCompatEditText(context, attrs) {


    private var isPasswordVisible: Boolean = false
    private val eyeOpenDrawable: Drawable
    private val eyeClosedDrawable: Drawable

    init {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT

        eyeOpenDrawable = ContextCompat.getDrawable(context, R.drawable.open_password)!!
        eyeClosedDrawable = ContextCompat.getDrawable(context, R.drawable.lock_password)!!

        setCompoundDrawablesWithIntrinsicBounds(null, null, eyeClosedDrawable, null)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(edit: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty() && s.length < 8) {
                    error = "Password must be at least 8 characters"
                }
            }
        })
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event?.let {
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (right - compoundPaddingRight)) {
                    togglePasswordVisibility()
                    return true
                }
            }
        }
        return super.onTouchEvent(event)
    }

    private fun togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
        if (isPasswordVisible) {
            inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            setCompoundDrawablesWithIntrinsicBounds(null, null, eyeOpenDrawable, null)
        } else {
            inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD or InputType.TYPE_CLASS_TEXT
            setCompoundDrawablesWithIntrinsicBounds(null, null, eyeClosedDrawable, null)
        }
        setSelection(text?.length ?: 0)
    }
}
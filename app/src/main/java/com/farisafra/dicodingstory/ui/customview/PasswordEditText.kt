package com.farisafra.dicodingstory.ui.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.farisafra.dicodingstory.R

class PasswordEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    init {
        setupTextChangedListener()
    }

    private fun setupTextChangedListener() {
        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.toString().length < 8) {
                    setError("Password tidak boleh kurang dari 8 karakter", null)
                } else {
                    error = null
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = text.toString()
                if (text.length < 8) {
                    error = "Password tidak boleh kurang dari 8 karakter"
                } else {
                    error = null
                }
            }
        }
    }
}
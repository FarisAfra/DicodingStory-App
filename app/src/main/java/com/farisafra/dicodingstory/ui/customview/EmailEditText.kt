package com.farisafra.dicodingstory.ui.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class EmailEditText(context: Context, attrs: AttributeSet) : AppCompatEditText(context, attrs) {

    init {
        setupEmailValidation()
    }

    private fun setupEmailValidation() {
        addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val text = s.toString()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    error = "Invalid email format"
                } else {
                    error = null
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        onFocusChangeListener = OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                val text = text.toString()
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
                    error = "Invalid email format"
                } else {
                    error = null
                }
            }
        }
    }
}
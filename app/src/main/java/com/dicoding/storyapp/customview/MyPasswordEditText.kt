package com.dicoding.storyapp.customview

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.dicoding.storyapp.R
import com.dicoding.storyapp.utils.passwordValidation
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyPasswordEditText : TextInputEditText {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        this@MyPasswordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputText = s.toString()
                if (inputText.isEmpty()) {
                    (parent.parent as TextInputLayout).error = null
                    (parent.parent as TextInputLayout).isErrorEnabled = false
                    this@MyPasswordEditText.setBackgroundResource(R.drawable.tv_border_black)
                } else {
                    if (passwordValidation(inputText)) {
                        (parent.parent as TextInputLayout).error = null
                        (parent.parent as TextInputLayout).isErrorEnabled = false
                        this@MyPasswordEditText.setBackgroundResource(R.drawable.tv_border_black)
                    } else {
                        (parent.parent as TextInputLayout).error =
                            context.getString(R.string.error_password)
                        (parent.parent as TextInputLayout).isErrorEnabled = true
                        this@MyPasswordEditText.setBackgroundResource(R.drawable.tv_border_red)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}
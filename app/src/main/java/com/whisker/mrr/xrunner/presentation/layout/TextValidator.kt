package com.whisker.mrr.xrunner.presentation.layout

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

abstract class TextValidator(private val editText: EditText) : TextWatcher {

    override fun afterTextChanged(s: Editable?) {
        val text = editText.text.toString()
        validate(text)
    }

    abstract fun validate(text: String)

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
}
package com.example.trazaconta.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.lang.ref.WeakReference
import java.text.NumberFormat
import java.util.*

class CurrencyMaskTextWatcher(val editText: EditText) : TextWatcher {
    private val editTextWeakReference: WeakReference<EditText> = WeakReference(editText)
    private var current = ""

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}

    override fun afterTextChanged(s: Editable) {
        val editText = editTextWeakReference.get() ?: return
        if (s.toString() != current) {
            editText.removeTextChangedListener(this)
            val cleanString = s.toString().replace("[R$,.\\s]".toRegex(), "")
            val parsed = cleanString.toDouble()
            val formatted = NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format((parsed / 100))
            current = formatted
            editText.setText(formatted)
            editText.setSelection(formatted.length)
            editText.addTextChangedListener(this)
        }
    }

    fun unmask() : Float
    {
        if(editText.text.toString().length>9)
        {
            return editText.text.toString().replace("[R$\\s]".toRegex(), "").replace(".","").replace(",",".").toFloat()
        }else
        {
            return editText.text.toString().replace("[R$\\s]".toRegex(), "").replace(",",".").toFloat()
        }

    }

}
package io.github.horaciocome1.reaque.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText

class OnFocusChangeListener(private val context: Context?) : View.OnFocusChangeListener {

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (view is TextInputEditText && !hasFocus) {
            val service = context?.getSystemService(Context.INPUT_METHOD_SERVICE)
            if (service is InputMethodManager)
                service.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

}
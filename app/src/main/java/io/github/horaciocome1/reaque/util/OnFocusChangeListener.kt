package io.github.horaciocome1.reaque.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputEditText

class OnFocusChangeListener(private val context: Context?) : View.OnFocusChangeListener {

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        view?.let {
            if (it is TextInputEditText && !hasFocus) {
                context?.getSystemService(Context.INPUT_METHOD_SERVICE).run {
                    if (this is InputMethodManager)
                        hideSoftInputFromWindow(it.windowToken, 0)
                }
            }
        }
    }

}
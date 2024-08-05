package com.mfitrahrmd.component

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class MyTextInputEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : TextInputEditText(context, attrs) {
    private val validator = mutableListOf<Validator>()

    init {
        val arr = context.obtainStyledAttributes(
            attrs,
            R.styleable.MyTextInputEditText,
            0,
            0
        )
        try {
            val validation = arr.getInteger(R.styleable.MyTextInputEditText_validation, VALIDATION_NONE)
            if (containsFlag(validation, VALIDATION_NOT_EMPTY)) {
                validator.add(NotEmptyValidator(text.toString()))
            }
            if (containsFlag(validation, VALIDATION_EMAIL)) {
                validator.add(EmailValidator(text.toString()))
            }
            if (containsFlag(validation, VALIDATION_PASSWORD)) {
                validator.add(PasswordValidator(text.toString()))
            }
            if (containsFlag(validation, VALIDATION_CONTAINS_NUMBER)) {
                validator.add(ContainsNumberValidator(text.toString()))
            }
        } finally {
            arr.recycle()
        }
        addTextChangedListener {
            val parent = parent.parent
            if (parent !is TextInputLayout) throw TypeCastException()
            parent.error = null
            validate(it.toString())
        }
    }

    fun validate(): Boolean {
        val parent = parent.parent
        if (parent !is TextInputLayout) throw TypeCastException()
        if (validator.isEmpty()) return true
        if (Validator.validate(text.toString(), *validator.toTypedArray())) {
            parent.error = null

            return true
        } else {
            parent.error = validator.mapNotNull { it.errorMessage }.joinToString("\n")

            return false
        }
    }

    private fun validate(txt: String): Boolean {
        val parent = parent.parent
        if (parent !is TextInputLayout) throw TypeCastException()
        if (validator.isEmpty()) return true
        if (Validator.validate(txt, *validator.toTypedArray())) {
            parent.error = null

            return true
        } else {
            parent.error = validator.mapNotNull { it.errorMessage }.joinToString("\n")

            return false
        }
    }

    private fun containsFlag(flagSet: Int, flag: Int): Boolean {
        return (flagSet or flag) == flagSet
    }

    companion object {
        private const val VALIDATION_NONE = 0
        private const val VALIDATION_NOT_EMPTY = 1
        private const val VALIDATION_EMAIL = 2
        private const val VALIDATION_PASSWORD = 4
        private const val VALIDATION_CONTAINS_NUMBER = 8
    }
}
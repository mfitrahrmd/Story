package com.mfitrahrmd.component

abstract class Validator(private var _text: String) {
    abstract var errorMessage: String?
    val text: String
        get() = _text

    abstract fun validate(): Boolean

    fun setText(text: String): Validator {
        this._text = text

        return this
    }

    companion object {
        fun validate(text: String, vararg validators: Validator): Boolean {
            val hasErrors = validators.map {
                it.setText(text).validate()
            }.contains(false)

            return !hasErrors
        }
    }
}
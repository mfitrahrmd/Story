package com.mfitrahrmd.component

import android.text.TextUtils
import android.util.Log

class NotEmptyValidator(t: String) : Validator(t) {
    override var errorMessage: String? = null

    override fun validate(): Boolean {
        if (TextUtils.isEmpty(text)) {
            errorMessage = "field cannot be empty"

            return false
        } else {
            errorMessage = null

            return true
        }
    }
}
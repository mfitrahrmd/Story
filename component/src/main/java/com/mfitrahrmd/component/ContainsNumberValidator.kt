package com.mfitrahrmd.component

import android.util.Log

class ContainsNumberValidator(t: String) : Validator(t) {
    override var errorMessage: String? = null

    override fun validate(): Boolean {
        if (!text.contains(Regex("\\d"))) {
            errorMessage = "field must contain numbers"

            return false
        } else {
            errorMessage = null

            return true
        }
    }
}

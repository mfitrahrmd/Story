package com.mfitrahrmd.component

class EmailValidator(email: String) : Validator(email) {
    override var errorMessage: String? = null

    override fun validate(): Boolean {
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            errorMessage = "email address is invalid"

            return false
        } else {
            errorMessage = null

            return true
        }
    }
}
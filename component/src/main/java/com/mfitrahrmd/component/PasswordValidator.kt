package com.mfitrahrmd.component

class PasswordValidator(password: String) : Validator(password) {
    override var errorMessage: String? = null
    private val minPasswordLength = 8
    private val maxPasswordLength = 16

    override fun validate(): Boolean {
        if (text.length < minPasswordLength) {
            errorMessage = "password must be at least $minPasswordLength characters long"

            return false
        } else if (text.length > maxPasswordLength) {
            errorMessage = "password must be at most $maxPasswordLength characters long"

            return false
        } else {
            errorMessage = null

            return true
        }
    }
}
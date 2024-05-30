package com.mfitrahrmd.story.data.entity

data class User(
    val id: String,
    val name: String,
    val account: Account?
) {
    data class Account(
        val email: String,
        val password: String,
        val token: String
    )
}

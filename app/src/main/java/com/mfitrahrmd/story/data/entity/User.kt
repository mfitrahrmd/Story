package com.mfitrahrmd.story.data.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String,
    val name: String,
    val account: Account?
) : Parcelable {
    @Parcelize
    data class Account(
        val email: String,
        val password: String,
        val token: String
    ) : Parcelable
}

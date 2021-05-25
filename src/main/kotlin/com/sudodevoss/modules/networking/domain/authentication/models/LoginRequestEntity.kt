package com.sudodevoss.easysignin.networking.domain.authentication.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestEntity(val username: String, val password: String)

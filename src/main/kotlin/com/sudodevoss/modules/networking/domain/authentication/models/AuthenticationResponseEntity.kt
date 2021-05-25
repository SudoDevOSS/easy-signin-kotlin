package com.sudodevoss.easysignin.networking.domain.authentication.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthenticationResponseEntity(val authorizationToken: String, val refreshToken: String)

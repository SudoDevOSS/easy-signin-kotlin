package com.sudodevoss.easysignin.networking.data.authentication.datasource

import com.kabdev.modules.networking.extensions.getBodyOrThrow
import com.kabdev.modules.networking.extensions.unwrapOrThrowIfNull
import com.sudodevoss.easysignin.networking.core.BaseJsonParser
import com.sudodevoss.easysignin.networking.core.BaseNetworking
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

internal class AuthenticationRemoteDataSourceImpl : AuthenticationDataSource {
    private fun parseAuthenticationResponse(response: Response): AuthenticationResponseEntity {
        return if (response.isSuccessful) {
            val body = response.getBodyOrThrow()
            val parsedBody = BaseJsonParser.parseJsonString<AuthenticationResponseEntity>(body)
            parsedBody.unwrapOrThrowIfNull()
        } else {
            throw BaseNetworking.parseNonSuccessHttpStatusCode(response.code)
        }
    }

    override suspend fun login(payload: LoginRequestEntity): AuthenticationResponseEntity =
        withContext(Dispatchers.IO) {
            val reqBody = BaseJsonParser.toJson(payload).toRequestBody("application/json".toMediaType())
            val request = BaseNetworking.newRequestBuilder("account/login").post(reqBody).build()
            val response = BaseNetworking.httpClient!!.newCall(request).execute()
            return@withContext parseAuthenticationResponse(response)
        }

    override suspend fun register(payload: RegisterRequestEntity): AuthenticationResponseEntity =
        withContext(Dispatchers.IO) {
            val reqBody = BaseJsonParser.toJson(payload).toRequestBody("application/json".toMediaType())
            val request = BaseNetworking.newRequestBuilder("account/register").post(reqBody).build()
            val response = BaseNetworking.httpClient!!.newCall(request).execute()
            return@withContext parseAuthenticationResponse(response)
        }

    override suspend fun resetPassword(email: String) {
        val request = BaseNetworking.newRequestBuilder("account/reset_password").get().build()
        val response = BaseNetworking.httpClient!!.newCall(request).execute()
        if (!response.isSuccessful) {
            throw BaseNetworking.parseNonSuccessHttpStatusCode(response.code)
        }
    }

    override suspend fun refreshToken(token: String, refreshToken: String): AuthenticationResponseEntity {
        val request = BaseNetworking.newRequestBuilder("account/refresh").build()
        val response = BaseNetworking.httpClient!!.newCall(request).execute()
        return parseAuthenticationResponse(response)
    }
}
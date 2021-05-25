package com.sudodevoss.easysignin.networking.data.account.datasource

import com.kabdev.modules.networking.extensions.getBodyOrThrow
import com.kabdev.modules.networking.extensions.unwrapOrThrowIfNull
import com.sudodevoss.easysignin.networking.core.BaseJsonParser
import com.sudodevoss.easysignin.networking.core.BaseNetworking
import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response

internal class AccountDataSourceImpl : AccountDataSource {
    private fun parseUserEntity(response: Response): UserEntity {
        return if (response.isSuccessful) {
            val body = response.getBodyOrThrow()
            val parsedBody = BaseJsonParser.parseJsonString<UserEntity>(body)
            parsedBody.unwrapOrThrowIfNull()
        } else {
            throw BaseNetworking.parseNonSuccessHttpStatusCode(response.code)
        }
    }

    override suspend fun fetch(): UserEntity = withContext(Dispatchers.IO) {
        val request = BaseNetworking.newRequestBuilder("account/info").build()
        val response = BaseNetworking.httpClient!!.newCall(request).execute()
        return@withContext parseUserEntity(response)
    }

    override suspend fun update(input: UserEntity): UserEntity = withContext(Dispatchers.IO) {
        val request = BaseNetworking
            .newRequestBuilder("account/info")
            .put(BaseJsonParser.toJson(input).toRequestBody("application/json".toMediaType()))
            .build()
        val response = BaseNetworking.httpClient!!.newCall(request).execute()
        return@withContext parseUserEntity(response)
    }
}
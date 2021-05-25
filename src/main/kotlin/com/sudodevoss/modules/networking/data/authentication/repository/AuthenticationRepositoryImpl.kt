package com.sudodevoss.easysignin.networking.data.authentication.repository

import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationDataSource
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result

internal class AuthenticationRepositoryImpl(private val authDataSource: AuthenticationDataSource) :
    AuthenticationRepository {
    override suspend fun login(payload: LoginRequestEntity): Result<AuthenticationResponseEntity, Throwable> =
        Result.Success(authDataSource.login(payload))

    override suspend fun register(payload: RegisterRequestEntity): Result<AuthenticationResponseEntity, Throwable> =
        Result.Success(authDataSource.register(payload))

    override suspend fun refreshToken(
        token: String,
        refreshToken: String
    ): Result<AuthenticationResponseEntity, Throwable> = Result.Success(authDataSource.refreshToken(token, refreshToken))

    override suspend fun resetPassword(email: String): Result<Boolean, Throwable> {
        authDataSource.resetPassword(email)
        return Result.Success(true)
    }
}
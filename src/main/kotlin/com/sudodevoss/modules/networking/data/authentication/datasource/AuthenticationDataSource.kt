package com.sudodevoss.easysignin.networking.data.authentication.datasource

import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity

internal interface AuthenticationDataSource {
    /**
     * Gets user authentication token
     *
     * @throws Exception
     * @return [AuthenticationResponseEntity]
     */
    suspend fun login(payload: LoginRequestEntity): AuthenticationResponseEntity

    /**
     * Register new user
     * @return [AuthenticationResponseEntity]
     */
    suspend fun register(payload: RegisterRequestEntity): AuthenticationResponseEntity

    /**
     * Request reset password
     *
     * @param email User email
     */
    suspend fun resetPassword(email: String)

    /**
     * Refresh user authentication token
     *
     * @return [AuthenticationResponseEntity]
     */
    suspend fun refreshToken(token: String, refreshToken: String): AuthenticationResponseEntity
}
package com.sudodevoss.easysignin.networking.domain.authentication.repository

import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Authentication Repository interface
 */
interface AuthenticationRepository {
    /**
     * Get user authentication token
     *
     * @param payload Login payload instance of [LoginRequestEntity]
     * @return [Result]<[AuthenticationRepository], [Throwable]> instance
     */
    suspend fun login(payload: LoginRequestEntity): Result<AuthenticationResponseEntity, Throwable>

    /**
     * Register user and get authentication token
     *
     * @param payload Register payload instance of [RegisterRequestEntity]
     * @return [Result]<[AuthenticationRepository], [Throwable]> instance
     */
    suspend fun register(payload: RegisterRequestEntity): Result<AuthenticationResponseEntity, Throwable>

    /**
     * Refresh user authentication token
     *
     * @param token Authentication token to refresh
     * @param refreshToken Refresh token of authentication token
     * @return [Result]<[AuthenticationRepository], [Throwable]> instance
     */
    suspend fun refreshToken(token: String, refreshToken: String): Result<AuthenticationResponseEntity, Throwable>

    /**
     * Request password reset link
     *
     * @param email User email
     * @return [Result]<[Boolean], [Throwable]> instance
     */
    suspend fun resetPassword(email: String): Result<Boolean, Throwable>
}
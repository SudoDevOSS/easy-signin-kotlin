package com.sudodevoss.easysignin.networking.domain.authentication.usecases

import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Register use case
 */
interface RegisterUseCase {
    /**
     * Invoke use case call
     *
     * @return [Result]>[AuthenticationResponseEntity],[Throwable]> instance
     */
    suspend operator fun invoke(payload: RegisterRequestEntity): Result<AuthenticationResponseEntity, Throwable>
}
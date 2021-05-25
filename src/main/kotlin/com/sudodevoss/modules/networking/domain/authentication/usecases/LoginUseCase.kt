package com.sudodevoss.easysignin.networking.domain.authentication.usecases

import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Login use case
 */
interface LoginUseCase {
    /**
     * Invoke use case call
     *
     * @return [Result]>[AuthenticationResponseEntity],[Throwable]> instance
     */
    suspend operator fun invoke(payload: LoginRequestEntity): Result<AuthenticationResponseEntity, Throwable>
}
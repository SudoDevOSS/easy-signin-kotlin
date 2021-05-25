package com.sudodevoss.easysignin.networking.domain.authentication.usecases

import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Reset Password use case
 */
interface ResetPasswordUseCase {
    /**
     * Invoke use case call
     *
     * @return [Result]>[Boolean],[Throwable]> instance
     */
    suspend operator fun invoke(email: String): Result<Boolean, Throwable>
}
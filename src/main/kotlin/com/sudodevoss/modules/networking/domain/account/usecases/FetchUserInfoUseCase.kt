package com.sudodevoss.easysignin.networking.domain.account.usecases

import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Fetch user info use case interface
 */
interface FetchUserInfoUseCase {
    /**
     * Invoke use case call
     *
     * @return [Result]<[UserEntity],[Throwable]>
     */
    suspend operator fun invoke(): Result<UserEntity, Throwable>
}
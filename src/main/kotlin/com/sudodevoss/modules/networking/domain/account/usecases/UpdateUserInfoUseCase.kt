package com.sudodevoss.easysignin.networking.domain.account.usecases

import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Update user info use case interface
 */
interface UpdateUserInfoUseCase {
    /**
     * Invoke use case call
     *
     * @param input [UserEntity] updated info
     * @return [Result]<[UserEntity],[Throwable]>
     */
    suspend operator fun invoke(input: UserEntity): Result<UserEntity, Throwable>
}
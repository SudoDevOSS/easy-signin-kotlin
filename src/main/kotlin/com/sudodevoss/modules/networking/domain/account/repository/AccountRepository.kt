package com.sudodevoss.easysignin.networking.domain.account.repository

import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result

/**
 * Account Repository interface
 */
interface AccountRepository {
    /**
     * Fetch user account information
     *
     * @return [Result]<[UserEntity], [Throwable]>
     */
    suspend fun fetch(): Result<UserEntity, Throwable>

    /**
     * Update user account information
     *
     * @return [Result]<[UserEntity], [Throwable]>
     */
    suspend fun update(input: UserEntity): Result<UserEntity, Throwable>
}
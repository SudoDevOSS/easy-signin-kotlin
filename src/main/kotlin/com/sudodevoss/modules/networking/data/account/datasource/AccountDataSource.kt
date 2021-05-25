package com.sudodevoss.easysignin.networking.data.account.datasource

import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity

internal interface AccountDataSource {
    /**
     * Fetch user account info
     *
     * @return [UserEntity] User info returned from server
     */
    suspend fun fetch(): UserEntity

    /**
     * Update user account info
     *
     * @param input Updated user info
     * @return [UserEntity] User info returned from server
     */
    suspend fun update(input: UserEntity): UserEntity
}
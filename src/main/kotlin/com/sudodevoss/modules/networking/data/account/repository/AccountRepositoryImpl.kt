package com.sudodevoss.easysignin.networking.data.account.repository

import com.sudodevoss.easysignin.networking.data.account.datasource.AccountDataSource
import com.sudodevoss.easysignin.networking.domain.account.repository.AccountRepository
import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import java.lang.Exception

internal class AccountRepositoryImpl(private val dataSource: AccountDataSource) : AccountRepository {
    override suspend fun fetch(): Result<UserEntity, Throwable> = Result.Success(dataSource.fetch())

    override suspend fun update(input: UserEntity): Result<UserEntity, Throwable> = Result.Success(dataSource.update(input))
}
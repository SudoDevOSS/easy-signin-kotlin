package com.sudodevoss.easysignin.networking.data.account.usecases

import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.account.repository.AccountRepository
import com.sudodevoss.easysignin.networking.domain.account.usecases.FetchUserInfoUseCase
import com.sudodevoss.easysignin.networking.domain.common.models.Result

class FetchUserInfoUseCaseImpl(private val repo: AccountRepository) : FetchUserInfoUseCase {
    override suspend fun invoke(): Result<UserEntity, Throwable> = repo.fetch()
}
package com.sudodevoss.easysignin.networking.data.account.usecases

import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.account.repository.AccountRepository
import com.sudodevoss.easysignin.networking.domain.account.usecases.UpdateUserInfoUseCase
import com.sudodevoss.easysignin.networking.domain.common.models.Result

class UpdateUserInfoUseCaseImpl(private val repo: AccountRepository) : UpdateUserInfoUseCase {
    override suspend fun invoke(input: UserEntity): Result<UserEntity, Throwable> = repo.update(input)
}
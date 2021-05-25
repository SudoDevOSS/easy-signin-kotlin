package com.sudodevoss.easysignin.networking.data.authentication.usecases

import com.sudodevoss.easysignin.networking.data.common.datasource.CacheDataSource
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.authentication.usecases.LoginUseCase
import com.sudodevoss.easysignin.networking.domain.common.models.Result

public class LoginUseCaseImpl(
    private val authRepo: AuthenticationRepository,
    private val cache: CacheDataSource<AuthenticationResponseEntity>? = null
) : LoginUseCase {
    public override suspend fun invoke(payload: LoginRequestEntity): Result<AuthenticationResponseEntity, Throwable> =
        authRepo.login(payload).also {
            if (it is Result.Success) {
                cache?.write(it.value)
            }
        }
}
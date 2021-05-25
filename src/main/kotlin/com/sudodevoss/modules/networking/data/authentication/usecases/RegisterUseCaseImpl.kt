package com.sudodevoss.easysignin.networking.data.authentication.usecases

import com.sudodevoss.easysignin.networking.data.common.datasource.CacheDataSource
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.authentication.usecases.RegisterUseCase
import com.sudodevoss.easysignin.networking.domain.common.models.Result

public class RegisterUseCaseImpl(
    private val authRepo: AuthenticationRepository,
    private val cache: CacheDataSource<AuthenticationResponseEntity>? = null
) : RegisterUseCase {
    public override suspend fun invoke(payload: RegisterRequestEntity): Result<AuthenticationResponseEntity, Throwable> =
        authRepo.register(payload).also {
            if (it is Result.Success) {
                cache?.write(it.value)
            }
        }
}
package com.sudodevoss.easysignin.networking.data.authentication.usecases

import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.authentication.usecases.ResetPasswordUseCase
import com.sudodevoss.easysignin.networking.domain.common.models.Result

public class ResetPasswordUseCaseImpl(private val authRepo: AuthenticationRepository) : ResetPasswordUseCase {
    public override suspend fun invoke(email: String): Result<Boolean, Throwable> = authRepo.resetPassword(email)
}
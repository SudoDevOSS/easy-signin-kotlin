package com.sudodevoss.easysignin.networking.data.authentication.usecases

import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationDataSource
import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationRemoteDataSourceImpl
import com.sudodevoss.easysignin.networking.data.authentication.repository.AuthenticationRepositoryImpl
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class ResetPasswordUseCaseTestImpl {
    companion object {

        @JvmStatic
        private val authenticationResponse = AuthenticationResponseEntity("DemoToken", "DemoRefreshToken")

        @JvmStatic
        internal val mockDataSource: AuthenticationDataSource = mockk<AuthenticationRemoteDataSourceImpl>()

        @JvmStatic
        val mockRepo: AuthenticationRepository = AuthenticationRepositoryImpl(mockDataSource).apply { spyk(this) }


        @BeforeAll
        @JvmStatic
        fun setup() {
            coEvery { mockRepo.resetPassword("demo@example.com") } returns (Result.Success(true))
        }
    }

    @Test
    fun `ResetPassword useCase`() = runBlocking {
        val useCase = ResetPasswordUseCaseImpl(mockRepo)
        val resetInfo = useCase("demo@example.com")
        Assertions.assertNotNull(resetInfo)
        Assertions.assertTrue(resetInfo is Result.Success)
        val info = (resetInfo as Result.Success).value
        Assertions.assertTrue(info)
    }
}
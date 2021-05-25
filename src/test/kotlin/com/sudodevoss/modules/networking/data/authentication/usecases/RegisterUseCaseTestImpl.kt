package com.sudodevoss.easysignin.networking.data.authentication.usecases

import com.sudodevoss.easysignin.networking.data.authentication.repository.AuthenticationRepositoryImpl
import com.sudodevoss.easysignin.networking.data.common.datasource.CacheDataSource
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class RegisterUseCaseTestImpl {
    companion object {

        @JvmStatic
        private val authenticationResponse = AuthenticationResponseEntity("DemoToken", "DemoRefreshToken")

        @JvmStatic
        val mockRepo: AuthenticationRepository = mockk<AuthenticationRepositoryImpl>()

        @JvmStatic
        val mockCacheDataSource = mockk<CacheDataSource<AuthenticationResponseEntity>>()

        @BeforeAll
        @JvmStatic
        fun setup() {
            coJustRun { mockCacheDataSource.write(authenticationResponse) }
            coEvery {
                mockRepo.register(
                    RegisterRequestEntity(
                        "demo",
                        "user",
                        "demo@example.com",
                        "+961-111111",
                        "p@ssw0rd",
                        "p@ssw0rd"
                    )
                )
            } returns (Result.Success(authenticationResponse))
        }
    }

    @Test
    fun `Register useCase`() = runBlocking {
        val useCase = RegisterUseCaseImpl(mockRepo, mockCacheDataSource)
        val registerInfo = useCase(
            RegisterRequestEntity(
                "demo",
                "user",
                "demo@example.com",
                "+961-111111",
                "p@ssw0rd",
                "p@ssw0rd"
            )
        )
        Assertions.assertNotNull(registerInfo)
        Assertions.assertTrue(registerInfo is Result.Success)

        val info = (registerInfo as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.refreshToken)
    }
}
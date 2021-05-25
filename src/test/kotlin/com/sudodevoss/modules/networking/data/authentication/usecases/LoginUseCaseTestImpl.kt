package com.sudodevoss.easysignin.networking.data.authentication.usecases

import com.sudodevoss.easysignin.networking.data.authentication.repository.AuthenticationRepositoryImpl
import com.sudodevoss.easysignin.networking.data.common.datasource.CacheDataSource
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class LoginUseCaseTestImpl {
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
                mockRepo.login(
                    LoginRequestEntity(
                        "demo@example.com",
                        "p@ssw0rd"
                    )
                )
            } returns (Result.Success(authenticationResponse))
        }
    }


    @Test
    fun `Login useCase`() = runBlocking {
        val useCase = LoginUseCaseImpl(mockRepo, mockCacheDataSource)
        val loginInfo = useCase(LoginRequestEntity("demo@example.com", "p@ssw0rd"))
        Assertions.assertNotNull(loginInfo)
        Assertions.assertTrue(loginInfo is Result.Success)

        val info = (loginInfo as Result.Success)
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.value.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.value.refreshToken)
    }
}
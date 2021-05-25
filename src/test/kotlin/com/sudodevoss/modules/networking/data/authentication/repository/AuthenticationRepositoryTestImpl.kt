package com.sudodevoss.easysignin.networking.data.authentication.repository

import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationDataSource
import com.sudodevoss.easysignin.networking.data.authentication.datasource.AuthenticationRemoteDataSourceImpl
import com.sudodevoss.easysignin.networking.data.common.exceptions.ServerException
import com.sudodevoss.easysignin.networking.data.common.exceptions.UnAuthorizedException
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.repository.AuthenticationRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class AuthenticationRepositoryTestImpl {
    companion object {

        @JvmStatic
        private val authenticationResponse = AuthenticationResponseEntity("DemoToken", "DemoRefreshToken")

        @JvmStatic
        internal val dataSource: AuthenticationDataSource = mockk<AuthenticationRemoteDataSourceImpl>()

        @JvmStatic
        val repo: AuthenticationRepository = AuthenticationRepositoryImpl(dataSource)


        @BeforeAll
        @JvmStatic
        fun setup() {
            coEvery {
                dataSource.login(
                    LoginRequestEntity(
                        "demo@example.com",
                        "p@ssw0rd"
                    )
                )
            } returns (authenticationResponse)
            coEvery {
                dataSource.register(
                    RegisterRequestEntity(
                        "demo",
                        "user",
                        "demo@example.com",
                        "+961-111111",
                        "p@ssw0rd",
                        "p@ssw0rd"
                    )
                )
            } returns (authenticationResponse)
            coEvery { dataSource.resetPassword("demo@example.com") } just runs
            coEvery { dataSource.refreshToken("DemoToken", "DemoRefreshToken") } returns (authenticationResponse)
        }
    }


    @Test
    fun `Login using repository`() = runBlocking {
        val loginPayload = LoginRequestEntity("demo@example.com", "p@ssw0rd")
        val loginInfo = repo.login(loginPayload)
        Assertions.assertNotNull(loginInfo)
        Assertions.assertTrue(loginInfo is Result.Success)

        val info = (loginInfo as Result.Success)
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.value.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.value.refreshToken)

        coVerify(exactly = 1) { dataSource.login(loginPayload) }
    }

    @Test
    fun `Register using repository`() = runBlocking {
        val registerPayload = RegisterRequestEntity(
            "demo",
            "user",
            "demo@example.com",
            "+961-111111",
            "p@ssw0rd",
            "p@ssw0rd"
        )
        val registerInfo = repo.register(registerPayload)
        Assertions.assertNotNull(registerInfo)
        Assertions.assertTrue(registerInfo is Result.Success)

        val info = (registerInfo as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.refreshToken)

        coVerify(exactly = 1) { dataSource.register(registerPayload) }
    }

    @Test
    fun `ResetPassword using repository`() = runBlocking {
        val email = "demo@example.com"
        val resetInfo = repo.resetPassword(email)
        Assertions.assertNotNull(resetInfo)
        Assertions.assertTrue(resetInfo is Result.Success)
        val info = (resetInfo as Result.Success).value
        Assertions.assertTrue(info)

        coVerify(exactly = 1) { dataSource.resetPassword(email) }
    }

    @Test
    fun `RefreshToken using repository`() = runBlocking {
        val t = "DemoToken"
        val r = "DemoRefreshToken"
        val refreshToken = repo.refreshToken(t, r)
        Assertions.assertNotNull(refreshToken)
        Assertions.assertTrue(refreshToken is Result.Success)

        val info = (refreshToken as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.refreshToken)

        coVerify(exactly = 1) { dataSource.refreshToken(t, r) }
    }


    @Test
    fun `Login using repository - throws unAuthorized`() {
        val loginPayload = LoginRequestEntity("demo@example.com", "p@ssw0rd")
        val dSource = mockk<AuthenticationDataSource>()
        coEvery {
            dSource.login(
                LoginRequestEntity(
                    "demo@example.com",
                    "p@ssw0rd"
                )
            )
        } throws (UnAuthorizedException())
        val repo = AuthenticationRepositoryImpl(dSource)
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { repo.login(loginPayload) }
        }

        coVerify(exactly = 1) { dataSource.login(loginPayload) }
    }

    @Test
    fun `Register using repository - throws bad request`() {
        val dSource = mockk<AuthenticationDataSource>()
        val registerPayload = RegisterRequestEntity(
            "demo",
            "user",
            "demo@example.com",
            "+961-111111",
            "p@ssw0rd",
            "p@ssw0rd"
        )
        val repo = AuthenticationRepositoryImpl(dSource)
        coEvery { dSource.register(registerPayload) } throws (IllegalArgumentException())
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking { repo.register(registerPayload) }
        }
    }

    @Test
    fun `ResetPassword using repository - throws server error`() {
        val dSource = mockk<AuthenticationDataSource>()
        val email = "demo@example.com"
        coEvery { dSource.resetPassword(email) } throws (ServerException())
        val repo = AuthenticationRepositoryImpl(dSource)
        Assertions.assertThrows(ServerException::class.java) {
            runBlocking { repo.resetPassword(email) }
        }
    }

    @Test
    fun `Refresh token using repository - throws server error`() {
        val dSource = mockk<AuthenticationDataSource>()
        val t = "DemoToken"
        val r = "DemoRefreshToken"
        coEvery {
            dSource.refreshToken(t, r)
        } throws (ServerException())
        val repo = AuthenticationRepositoryImpl(dSource)
        Assertions.assertThrows(ServerException::class.java) {
            runBlocking { repo.refreshToken(t, r) }
        }

        coVerify(exactly = 1) { dataSource.refreshToken(t, r) }
    }
}
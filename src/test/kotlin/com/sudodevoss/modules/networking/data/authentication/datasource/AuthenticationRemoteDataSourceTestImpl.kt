package com.sudodevoss.easysignin.networking.data.authentication.datasource

import com.sudodevoss.easysignin.networking.core.BaseJsonParser
import com.sudodevoss.easysignin.networking.core.BaseNetworkingMockConfigurator
import com.sudodevoss.easysignin.networking.data.common.exceptions.ServerException
import com.sudodevoss.easysignin.networking.data.common.exceptions.UnAuthorizedException
import com.sudodevoss.easysignin.networking.domain.authentication.models.AuthenticationResponseEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.LoginRequestEntity
import com.sudodevoss.easysignin.networking.domain.authentication.models.RegisterRequestEntity
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException

class AuthenticationRemoteDataSourceTestImpl {
    private val dummyEmail = "demo@example.com"
    companion object {
        @JvmStatic
        val mockServer = BaseNetworkingMockConfigurator()

        @JvmStatic
        private val authenticationResponse =
            AuthenticationResponseEntity("DemoToken", "DemoRefreshToken")

        @JvmStatic
        internal val dataSource: AuthenticationDataSource = AuthenticationRemoteDataSourceImpl()

        @BeforeAll
        @JvmStatic
        fun setup() {
            mockServer.startServer()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            mockServer.shutdownServer()
        }
    }
    
    @Test
    fun `Login using dataSource`() = runBlocking {
        val mockLoginRequest = MockResponse().apply { setBody(BaseJsonParser.toJson(authenticationResponse)) }
        mockServer.mockResponse(mockLoginRequest, "account/login")
        val info = dataSource.login(LoginRequestEntity(dummyEmail, "password"))
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.refreshToken)

        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/login") { "Found this path: ${req.path}" }
    }

    @Test
    fun `Register using dataSource`() = runBlocking {
        val mockRegisterRequest = MockResponse().apply { setBody(BaseJsonParser.toJson(authenticationResponse)) }
        mockServer.mockResponse(mockRegisterRequest, "account/register")
        val info = dataSource.register(
            RegisterRequestEntity(
                "Demo",
                "user",
                dummyEmail,
                "+961-111111",
                "p@ssw0rd",
                "p@ssw0rd"
            )
        )
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoToken", info.authorizationToken)
        Assertions.assertEquals("DemoRefreshToken", info.refreshToken)

        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/register") { "Found this path: ${req.path}" }
    }

    @Test
    fun `ResetPassword using dataSource`() = runBlocking {
        val mockResetPasswordRequest = MockResponse()
        mockServer.mockResponse(mockResetPasswordRequest, "account/reset_password")
        dataSource.resetPassword(dummyEmail)
        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/reset_password") { "Found this path: ${req.path}" }
    }

    @Test
    fun `RefreshToken using dataSource`() = runBlocking {
        val mockRegisterRequest = MockResponse().apply {
            setBody(
                BaseJsonParser.toJson(
                    AuthenticationResponseEntity(
                        "DemoTokenUpdated",
                        "DemoRefreshTokenUpdated"
                    )
                )
            )
        }
        mockServer.mockResponse(mockRegisterRequest, "account/refresh")
        val info = dataSource.refreshToken("DemoToken", "DemoRefreshToken")
        Assertions.assertNotNull(info)
        Assertions.assertEquals("DemoTokenUpdated", info.authorizationToken)
        Assertions.assertEquals("DemoRefreshTokenUpdated", info.refreshToken)

        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/refresh") { "Found this path: ${req.path}" }
    }

    @Test
    fun `Login using dataSource - throws unAuthorized`() {
        val mockLoginRequest = MockResponse().apply { setResponseCode(401) }
        mockServer.mockResponse(mockLoginRequest, "account/login")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.login(LoginRequestEntity(dummyEmail, "password")) }
        }
        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/login") { "Found this path: ${req.path}" }
    }

    @Test
    fun `Register using dataSource - throws bad request`() {
        val mockRegisterRequest = MockResponse().apply { setResponseCode(400) }
        mockServer.mockResponse(mockRegisterRequest, "account/register")
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            runBlocking {
                dataSource.register(
                    RegisterRequestEntity(
                        "Demo",
                        "user",
                        dummyEmail,
                        "+961-111111",
                        "p@ssw0rd",
                        "p@ssw0rd"
                    )
                )
            }
        }
        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/register") { "Found this path: ${req.path}" }
    }

    @Test
    fun `ResetPassword using dataSource - throws server error`() {
        val mockResetPasswordRequest = MockResponse().apply { setResponseCode(500) }
        mockServer.mockResponse(mockResetPasswordRequest, "account/reset_password")
        Assertions.assertThrows(ServerException::class.java) {
            runBlocking { dataSource.resetPassword(dummyEmail) }
        }
        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/reset_password") { "Found this path: ${req.path}" }
    }

    @Test
    fun `RefreshToken using dataSource - throws server error`() {
        val mockRegisterRequest = MockResponse().apply { setResponseCode(500) }
        mockServer.mockResponse(mockRegisterRequest, "account/refresh")
        Assertions.assertThrows(ServerException::class.java) {
            runBlocking { dataSource.refreshToken("DemoToken", "DemoRefreshToken") }
        }
        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/refresh") { "Found this path: ${req.path}" }
    }
}
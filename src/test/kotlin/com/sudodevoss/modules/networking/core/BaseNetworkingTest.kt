package com.sudodevoss.easysignin.networking.core

import com.sudodevoss.easysignin.networking.data.common.exceptions.ServerException
import io.mockk.every
import io.mockk.mockk
import okhttp3.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.lang.IllegalArgumentException
import java.lang.NullPointerException


class BaseNetworkingTest {
    private val demoUrl = "http://someurl.local"
    @BeforeEach
    fun resetBaseNetworking() {
        val httpClientField = BaseNetworking::class.java.getDeclaredField("httpClient")
        httpClientField.isAccessible = true
        httpClientField.set(null, null)

        val urlField = BaseNetworking::class.java.getDeclaredField("baseUrl")
        urlField.isAccessible = true
        urlField.set(null, "")
    }

    @Test
    fun `Test Configuring HttpClient`() {
        Assertions.assertAll(
            Executable { BaseNetworking.configureClient(demoUrl, "BaseToken") },
            Executable { Assertions.assertNotNull(BaseNetworking.httpClient) },
            Executable { Assertions.assertNotNull(BaseNetworking.httpClient?.authenticator) }
        )
    }

    @Test
    fun `Test Configuring HttpClient - Throws - HttpClient not configured`() {
        Assertions.assertThrows(NullPointerException::class.java) {
            BaseNetworking.httpClient
        }
    }

    @Test
    fun `Test Configuring HttpClient Authenticator`() {
        val demoUrl = "demoUrl/"
        val mockRoute = mockk<Route>()
        val mockResponse = mockk<Response>()

        every { mockRoute.address } returns (mockk())
        every { mockRoute.address.url } returns (mockk())
        every { mockRoute.address.url.encodedPath } returns (demoUrl)
        every { mockResponse.request } returns (Request.Builder().url(demoUrl).build())

        BaseNetworking.configureClient(demoUrl, "DemoToken")
        Assertions.assertNotNull(BaseNetworking.httpClient)
        Assertions.assertNotNull(BaseNetworking.httpClient?.authenticator)
        val response = BaseNetworking.httpClient?.authenticator?.authenticate(mockRoute, mockResponse)
        Assertions.assertNotNull(response)
        Assertions.assertNotNull(response?.header("Authorization"))
        Assertions.assertEquals("Bearer DemoToken", response?.header("Authorization"))
    }

    @Test
    fun `Test configure http client no authentication`() {
        val mockRoute = mockk<Route>()
        val mockResponse = mockk<Response>()

        every { mockRoute.address } returns (mockk())
        every { mockRoute.address.url } returns (mockk())
        every { mockRoute.address.url.encodedPath } returns ("${demoUrl}login")
        every { mockResponse.request } returns (Request.Builder().url(demoUrl).build())

        BaseNetworking.configureClient(demoUrl, "DemoToken")
        val notAuthorizedResponse = BaseNetworking.httpClient?.authenticator?.authenticate(mockRoute, mockResponse)
        Assertions.assertNotNull(notAuthorizedResponse)
        Assertions.assertNull(notAuthorizedResponse?.header("Authorization"))
    }

    @Test
    fun `Test configure http client - throws - null url configuration`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            BaseNetworking.configureClient("", "")
        }
    }

    @Test
    fun `Test Parse Status Code - Throws - ServerException`() {
        Assertions.assertTrue(BaseNetworking.parseNonSuccessHttpStatusCode((500..599).random()) is ServerException)
        Assertions.assertThrows(ServerException::class.java) { throw BaseNetworking.parseNonSuccessHttpStatusCode((500..599).random()) }
    }

    @Test
    fun `Test new request builder - throws - null url configuration`() {
        Assertions.assertThrows(NullPointerException::class.java) {
            BaseNetworking.newRequestBuilder("Test")
        }
    }

    @Test
    fun `Test new request builder`() {
        BaseNetworking.configureClient("http://local.local", "")
        Assertions.assertAll(
            Executable { BaseNetworking.newRequestBuilder("Test") }
        )
    }

    @Test
    fun `Test configuring httpClient - throws illegalArgument`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            BaseNetworking.configureClient("", "BaseToken")
        }
    }
}
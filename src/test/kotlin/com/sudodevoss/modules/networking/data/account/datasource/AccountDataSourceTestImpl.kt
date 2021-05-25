package com.sudodevoss.easysignin.networking.data.account.datasource

import com.sudodevoss.easysignin.networking.core.BaseJsonParser
import com.sudodevoss.easysignin.networking.core.BaseNetworkingMockConfigurator
import com.sudodevoss.easysignin.networking.data.common.exceptions.ServerException
import com.sudodevoss.easysignin.networking.data.common.exceptions.UnAuthorizedException
import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

class AccountDataSourceTestImpl {
    private val dummyEmail = "demoz@example.com"

    companion object {

        @JvmStatic
        val mockServer = BaseNetworkingMockConfigurator()

        @JvmStatic
        private fun generateDummyUser() = UserEntity(
            UUID.randomUUID().toString(),
            "Demo",
            "User",
            "demo@example.com",
            "+961-111111"
        )

        @JvmStatic
        internal val dataSource: AccountDataSource = AccountDataSourceImpl()


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
    fun `Fetching userInfo from server using dataSource`() = runBlocking {
        val mockFetchInfoResponse = MockResponse().apply { setBody(BaseJsonParser.toJson(generateDummyUser())) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        val info = dataSource.fetch()
        Assertions.assertNotNull(info)
        Assertions.assertEquals("Demo", info.firstName)
        Assertions.assertEquals("User", info.lastName)
        Assertions.assertEquals("demo@example.com", info.email)

        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/info") { "Found this path: ${req.path}" }
    }

    @Test
    fun `Update userInfo from server using dataSource`() = runBlocking {
        val updatedEntity = generateDummyUser().apply {
            changeEmail("updated@example.com")
            changeName("UnDemo", "A User")
        }
        val mockFetchInfoResponse = MockResponse().apply { setBody(BaseJsonParser.toJson(updatedEntity)) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")

        val info = dataSource.update(updatedEntity)
        Assertions.assertNotNull(info)
        Assertions.assertEquals("UnDemo", info.firstName)
        Assertions.assertEquals("A User", info.lastName)
        Assertions.assertEquals("updated@example.com", info.email)

        val req = mockServer.takeRequest()
        Assertions.assertEquals(req.path, "/account/info") { "Found this path: ${req.path}" }
    }

    @Test
    fun `Fetch userInfo failed - not authorized`() {
        var mockFetchInfoResponse = MockResponse().apply { setResponseCode(401) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.fetch() }
        }

        mockFetchInfoResponse = MockResponse().apply { setResponseCode(403) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.fetch() }
        }
    }

    @Test
    fun `Fetch userInfo failed - server error`() {
        val mockFetchInfoResponse = MockResponse().apply { setResponseCode(500) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        Assertions.assertThrows(ServerException::class.java) {
            runBlocking { dataSource.fetch() }
        }
    }

    @Test
    fun `Fetch userInfo failed - not found`() {
        val mockFetchInfoResponse = MockResponse().apply { setResponseCode(404) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        Assertions.assertThrows(Exception::class.java) {
            runBlocking { dataSource.fetch() }
        }
    }

    @Test
    fun `Update userInfo failed - not authorized`() {
        var mockUpdateUserInfoResponse = MockResponse().apply { setResponseCode(401) }
        mockServer.mockResponse(mockUpdateUserInfoResponse, "account/info")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.update(generateDummyUser().apply { changeEmail(dummyEmail) }) }
        }

        mockUpdateUserInfoResponse = MockResponse().apply { setResponseCode(403) }
        mockServer.mockResponse(mockUpdateUserInfoResponse, "account/info")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.update(generateDummyUser().apply { changeEmail(dummyEmail) }) }
        }
    }

    @Test
    fun `Update userInfo failed - server error`() {
        val mockUpdateUserInfoResponse = MockResponse().apply { setResponseCode(500) }
        mockServer.mockResponse(mockUpdateUserInfoResponse, "account/info")
        Assertions.assertThrows(ServerException::class.java) {
            runBlocking { dataSource.update(generateDummyUser().apply { changeEmail(dummyEmail) }) }
        }
    }

    @Test
    fun `Update userInfo failed - not found`() {
        val mockUpdateUserInfoResponse = MockResponse().apply { setResponseCode(404) }
        mockServer.mockResponse(mockUpdateUserInfoResponse, "account/info")
        Assertions.assertThrows(Exception::class.java) {
            runBlocking { dataSource.update(generateDummyUser().apply { changeEmail(dummyEmail) }) }
        }
    }

    @Test
    fun `Fetch userInfo failed - unAuthorized`() {
        val mockFetchInfoResponse = MockResponse().apply { setResponseCode(401) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.fetch() }
        }
    }

    @Test
    fun `Update userInfo failed - unAuthorized`() {
        val mockUpdateUserInfoResponse = MockResponse().apply { setResponseCode(401) }
        mockServer.mockResponse(mockUpdateUserInfoResponse, "account/info")
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { dataSource.update(generateDummyUser()) }
        }
    }
}
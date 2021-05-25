package com.sudodevoss.easysignin.networking.data.account.datasource

import com.sudodevoss.easysignin.networking.core.BaseJsonParser
import com.sudodevoss.easysignin.networking.core.BaseNetworking
import com.sudodevoss.easysignin.networking.core.BaseNetworkingMockConfigurator
import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.io.IOException
import java.net.InetAddress
import java.util.*

class AccountDataSourceTestImplFailure {

    companion object {
        @JvmStatic
        val mockServer = BaseNetworkingMockConfigurator()

        @JvmStatic
        internal val dataSource: AccountDataSource = AccountDataSourceImpl()

        @JvmStatic
        private val userEntity = UserEntity(
            UUID.randomUUID().toString(),
            "Demo",
            "User",
            "demo@example.com",
            "+961-111111"
        )
    }

    @Test
    fun `Fetch userInfo failed to connect server down`() {
        BaseNetworking.configureClient("http://${InetAddress.getLoopbackAddress()}:8081/", "")
        val mockFetchInfoResponse = MockResponse().apply { setBody(BaseJsonParser.toJson(userEntity)) }
        mockServer.mockResponse(mockFetchInfoResponse, "account/info")
        Assertions.assertThrows(IOException::class.java) {
            runBlocking { dataSource.fetch() }
        }
    }
}
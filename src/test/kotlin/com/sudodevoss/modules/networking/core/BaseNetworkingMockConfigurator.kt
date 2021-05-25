package com.sudodevoss.easysignin.networking.core

import com.sun.tools.sjavac.Log
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import java.lang.Exception
import java.net.InetAddress
import kotlin.random.Random

/**
 * Mocks [BaseNetworking] and starts a [MockWebServer] on a random port in range 5000..10000
 */
class BaseNetworkingMockConfigurator {
    private val mockWebServer = MockWebServer()
    private var serverStarted = false

    /**
     * Enqueue a [MockResponse] on [MockWebServer] on a given endpoint path
     */
    fun mockResponse(response: MockResponse, path: String) {
        mockWebServer.enqueue(response)
        mockWebServer.url(path)
    }

    /**
     * Starts [MockWebServer] on random port in range 5000..10000
     */
    fun startServer() {
        val port = (5000..10000).random()
        if (!serverStarted) {
            try {
                mockWebServer.start(InetAddress.getLoopbackAddress(), port)
            } catch (ex: Exception) {
                Log.debug(ex.message)
            }
            BaseNetworking.configureClient("http://${InetAddress.getLoopbackAddress().hostAddress}:$port/", "")
        }
    }

    /**
     * Gets a [RecordedRequest] from [MockWebServer]
     *
     * @return [RecordedRequest]
     */
    fun takeRequest(): RecordedRequest {
        return mockWebServer.takeRequest()
    }

    /**
     * Shuts down [MockWebServer]
     */
    fun shutdownServer() {
        if (serverStarted) {
            mockWebServer.shutdown()
            serverStarted = false
        }
    }
}
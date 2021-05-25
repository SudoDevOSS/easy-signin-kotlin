package com.sudodevoss.easysignin.networking.data.account.usecases

import com.sudodevoss.easysignin.networking.data.account.repository.AccountRepositoryImpl
import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.account.repository.AccountRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.spyk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import java.util.*

class FetchUserInfoUseCaseTest {
    companion object {

        @JvmStatic
        private fun generateDummyUser() = UserEntity(
            UUID.randomUUID().toString(),
            "Demo",
            "User",
            "demo@example.com",
            "+961-111111"
        )

        @JvmStatic
        private var mockFetchUserInfoRepository: AccountRepository = mockk<AccountRepositoryImpl>()

    }


    @Test
    fun `Fetching userInfo from server useCase`() = runBlocking {
        coEvery { mockFetchUserInfoRepository.fetch() } returns (Result.Success(generateDummyUser()))
        val fetchUserInfoUseCase = FetchUserInfoUseCaseImpl(mockFetchUserInfoRepository)
        val userInfo = fetchUserInfoUseCase()
        Assertions.assertNotNull(userInfo)
        Assertions.assertTrue(userInfo is Result.Success) { (userInfo as Result.Failure).reason.message }

        val info = (userInfo as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("Demo", info.firstName)
        Assertions.assertEquals("User", info.lastName)
        Assertions.assertEquals("demo@example.com", info.email)

        coVerify(exactly = 1) { mockFetchUserInfoRepository.fetch() }
    }
}
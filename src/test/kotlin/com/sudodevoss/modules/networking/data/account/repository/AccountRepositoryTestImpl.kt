package com.sudodevoss.easysignin.networking.data.account.repository

import com.sudodevoss.easysignin.networking.data.account.datasource.AccountDataSource
import com.sudodevoss.easysignin.networking.data.account.datasource.AccountDataSourceImpl
import com.sudodevoss.easysignin.networking.data.common.exceptions.UnAuthorizedException
import com.sudodevoss.easysignin.networking.domain.account.models.UserEntity
import com.sudodevoss.easysignin.networking.domain.account.repository.AccountRepository
import com.sudodevoss.easysignin.networking.domain.common.models.Result
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

class AccountRepositoryTestImpl {
    private val dummyUpdateEmail = "updated@example.com"
    companion object {
        private const val dummyPhoneNumber = "+961-111111"
        internal val dataSource: AccountDataSource = mockk<AccountDataSourceImpl>()

        @JvmStatic
        private fun generateDummyUser() = UserEntity(
            UUID.randomUUID().toString(),
            "Demo",
            "User",
            "demo@example.com",
            dummyPhoneNumber
        )

        @JvmStatic
        private lateinit var userInfoRepository: AccountRepository

        @BeforeAll
        @JvmStatic
        fun setup() {
            userInfoRepository = AccountRepositoryImpl(dataSource)
            coEvery { dataSource.fetch() } returns (generateDummyUser())
        }
    }


    @Test
    fun `Fetching userInfo from server using repository`() = runBlocking {
        val userInfo = userInfoRepository.fetch()
        Assertions.assertNotNull(userInfo)
        Assertions.assertTrue(userInfo is Result.Success) { (userInfo as Result.Failure).reason.message }

        val info = (userInfo as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("Demo", info.firstName)
        Assertions.assertEquals("User", info.lastName)
        Assertions.assertEquals("demo@example.com", info.email)

        coVerify(exactly = 1) { dataSource.fetch() }
    }

    @Test
    fun `Update userInfo from server using repository`() = runBlocking {
        val user = generateDummyUser()
        val updatedEntity = user.apply {
            changeEmail(dummyUpdateEmail)
            changeName("UnDemo", "A User")
            changePhoneNumber(dummyPhoneNumber)
        }
        coEvery { dataSource.update(user) } returns (updatedEntity)
        val userInfo = userInfoRepository.update(updatedEntity)
        Assertions.assertNotNull(userInfo)
        Assertions.assertTrue(userInfo is Result.Success) { (userInfo as Result.Failure).reason.message }

        val info = (userInfo as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("UnDemo", info.firstName)
        Assertions.assertEquals("A User 2", info.lastName)
        Assertions.assertEquals(dummyUpdateEmail, info.email)

        coVerify(exactly = 1) { dataSource.update(updatedEntity) }
    }

    @Test
    fun `Update userInfo from server - unAuthorized`() {
        val dSource = mockk<AccountDataSource>()
        val mockFetchUserInfoRepository = AccountRepositoryImpl(dSource)
        val user = generateDummyUser()
        val updatedEntity = user.apply {
            changeEmail(dummyUpdateEmail)
            changeName("UnDemo", "A User 3")
            changePhoneNumber(dummyPhoneNumber)
        }
        coEvery { dSource.update(user) } throws (UnAuthorizedException())
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { mockFetchUserInfoRepository.update(updatedEntity) }
        }

        coVerify(exactly = 1) { dSource.update(updatedEntity) }
    }

    @Test
    fun `Fetch userInfo from server - unAuthorized`() {
        val dSource = mockk<AccountDataSource>()
        val mockFetchUserInfoRepository = AccountRepositoryImpl(dSource)
        coEvery { dSource.fetch() } throws UnAuthorizedException()
        Assertions.assertThrows(UnAuthorizedException::class.java) {
            runBlocking { mockFetchUserInfoRepository.fetch() }
        }

        coVerify(exactly = 1) { dSource.fetch() }
    }
}
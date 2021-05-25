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
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import java.util.*

class UpdateUserInfoUseCaseTest {
    companion object {

        @JvmStatic
        private val userEntity = UserEntity(
            UUID.randomUUID().toString(),
            "Demo",
            "User",
            "demo@example.com",
            "+961-111111"
        ).run { spyk(this) }

        @JvmStatic
        private var mockUpdateUserInfoRepository: AccountRepository = mockk<AccountRepositoryImpl>()

        @BeforeAll
        @JvmStatic
        fun setup() {
            coEvery { mockUpdateUserInfoRepository.update(userEntity) } returns (Result.Success(userEntity.apply {
                changeName("A Demo", "Updated")
                changeEmail("updated@example.com")
            }))
        }
    }

    @Test
    fun `Update userInfo on server useCase`() = runBlocking {
        val updateUserInfoUseCase = UpdateUserInfoUseCaseImpl(mockUpdateUserInfoRepository)
        val userInfo = updateUserInfoUseCase(userEntity.apply {
            changeName("A Demo 2", "Updated")
            changeEmail("updated2@example.com")
        })
        Assertions.assertNotNull(userInfo)
        Assertions.assertTrue(userInfo is Result.Success) { (userInfo as Result.Failure).reason.message }

        val info = (userInfo as Result.Success).value
        Assertions.assertNotNull(info)
        Assertions.assertEquals("A Demo 2", info.firstName)
        Assertions.assertEquals("Updated", info.lastName)
        Assertions.assertEquals("updated2@example.com", info.email)

        coVerify(exactly = 1) { mockUpdateUserInfoRepository.update(userEntity) }
    }
}
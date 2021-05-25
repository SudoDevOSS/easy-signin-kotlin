package com.sudodevoss.easysignin.networking.domain.account.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable
import java.lang.IllegalArgumentException

class UserEntityTestImpl {

    @Test
    fun `Change Phone Number - Throws`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            UserEntity("ID", "F", "A", "email@example.com", "123").apply {
                changePhoneNumber("")
            }
        }
    }

    @Test
    fun `Change phone number - just runs`() {
        UserEntity("ID", "F", "A", "email@example.com", "123").apply {
            changePhoneNumber("123321")
        }.apply { Assertions.assertEquals("123321", this.phoneNumber) }
    }

    @Test
    fun `Read props - just runs`() {
        UserEntity("ID", "F", "A", "email@example.com", "123").apply {
            Assertions.assertEquals("F", firstName)
            Assertions.assertEquals("A", lastName)
            Assertions.assertEquals("email@example.com", email)
            Assertions.assertEquals("123", phoneNumber)
        }
    }

    @Test
    fun `Change email - throws`() {
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            UserEntity("ID", "F", "A", "email@example.com", "123").apply {
                changeEmail("")
            }
        }
    }

    @Test
    fun `Change Email - Just runs`() {
        UserEntity("ID", "F", "A", "email@example.com", "123").apply {
            changeEmail("demo@example.com")
        }.apply {
            Assertions.assertEquals("demo@example.com", this.email)
        }
    }

    @Test
    fun `Change Name - Just runs`() {
        val user = UserEntity("ID", "F", "A", "email@example.com", "123").apply {
            changeName()
        }
        Assertions.assertAll(
            Executable { Assertions.assertEquals("F", user.firstName) },
            Executable { Assertions.assertEquals("A", user.lastName) }
        )
    }
}
package com.sudodevoss.easysignin.networking.domain.authentication.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RegistrationRequestEntityTestImpl {
    @Test
    fun `Create registrationEntity`() {
        val entity = RegisterRequestEntity(
            "F", "L",
            "e@example.com", "123",
            "pass", "pass"
        )

        Assertions.assertEquals("F", entity.firstName)
        Assertions.assertEquals("L", entity.lastName)
        Assertions.assertEquals("123", entity.phoneNumber)
        Assertions.assertEquals("e@example.com", entity.email)
        Assertions.assertEquals("pass", entity.password)
        Assertions.assertEquals("pass", entity.confirmPassword)

    }
}
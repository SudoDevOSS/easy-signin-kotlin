package com.sudodevoss.easysignin.networking.domain.authentication.models

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LoginRequestEntityTestImpl {
    @Test
    fun `Read props - just runs`() {
        LoginRequestEntity("email@example.com", "password").apply {
            Assertions.assertEquals(username, "email@example.com")
            Assertions.assertEquals(password, "password")
        }
    }
}
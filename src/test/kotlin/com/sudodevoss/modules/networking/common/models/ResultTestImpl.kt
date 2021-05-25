package com.sudodevoss.easysignin.networking.common.models

import com.sudodevoss.easysignin.networking.domain.common.models.Result
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class ResultTestImpl {

    @Test
    fun `Creates failure with reason`() {
        val failure = Result.Failure(IllegalArgumentException())
        Assertions.assertTrue(failure.reason is IllegalArgumentException)
    }
}
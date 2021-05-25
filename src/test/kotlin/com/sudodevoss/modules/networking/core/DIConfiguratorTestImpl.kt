package com.sudodevoss.easysignin.networking.core

import com.sudodevoss.easysignin.networking.ioc.DIConfigurator
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.function.Executable

class DIConfiguratorTestImpl {
    @Test
    fun `Test modules length`() {
        Assertions.assertTrue(DIConfigurator.getDIModules().count() > 0)
        Assertions.assertAll(
            Executable { DIConfigurator.getDIModules() }
        )
    }
}
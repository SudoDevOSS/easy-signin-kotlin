package com.sudodevoss.easysignin.networking.core

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class BaseJsonParserTestImpl {
    @Test
    fun `Moshi instance not null`() {
        Assertions.assertNotNull(BaseJsonParser.moshi)
        Assertions.assertNotNull(BaseJsonParser)
    }
}
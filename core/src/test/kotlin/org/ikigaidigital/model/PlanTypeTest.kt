package org.ikigaidigital.model

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource

class PlanTypeTest {

    @ParameterizedTest
    @CsvSource(
        value = [
            "basic,   BASIC",
            "student, STUDENT",
            "premium, PREMIUM",
        ]
    )
    fun `fromCode maps known codes`(code: String, expected: PlanType) {
        assertThat(PlanType.fromCode(code)).isEqualTo(expected)
    }

    @ParameterizedTest
    @ValueSource(strings = ["gold", "Basic", "PREMIUM", ""])
    fun `fromCode returns null for unknown or wrongly cased codes`(code: String) {
        assertThat(PlanType.fromCode(code)).isNull()
    }
}

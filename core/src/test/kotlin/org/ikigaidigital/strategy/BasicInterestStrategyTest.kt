package org.ikigaidigital.strategy

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.fixture.TimeDepositFixtures.basicTimeDeposit
import org.ikigaidigital.model.PlanType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource

class BasicInterestStrategyTest {

    private val strategy = BasicInterestStrategy()

    @Test
    fun `handles basic plan type`() {
        assertThat(strategy.planType).isEqualTo(PlanType.BASIC)
    }

    @ParameterizedTest
    @MethodSource("interestProvider")
    fun `calculateMonthlyInterest returns monthly interest rounded to cents`(deposit: TimeDeposit, expectedInterest: Double) {
        assertThat(strategy.calculateMonthlyInterest(deposit)).isEqualTo(expectedInterest)
    }

    companion object {
        @JvmStatic
        fun interestProvider(): List<Arguments> = listOf(
            argumentSet("day 30 – no interest yet", basicTimeDeposit(days = 30, balance = 1200.0), 0.0),
            argumentSet("day 31 – earns interest", basicTimeDeposit(days = 31, balance = 1200.0), 1.0),
            argumentSet("day 366 – still earns", basicTimeDeposit(days = 366, balance = 1200.0), 1.0),
            argumentSet("interest is rounded", basicTimeDeposit(days = 31, balance = 100.0), 0.08),
            argumentSet("zero balance – zero interest", basicTimeDeposit(days = 31, balance = 0.0), 0.0),
        )
    }
}

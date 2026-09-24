package org.ikigaidigital.strategy

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.fixture.TimeDepositFixtures.premiumTimeDeposit
import org.ikigaidigital.model.PlanType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource

class PremiumInterestStrategyTest {

    private val strategy = PremiumInterestStrategy()

    @Test
    fun `handles premium plan type`() {
        assertThat(strategy.planType).isEqualTo(PlanType.PREMIUM)
    }

    @ParameterizedTest
    @MethodSource("interestProvider")
    fun `calculateMonthlyInterest returns monthly interest rounded to cents`(deposit: TimeDeposit, expectedInterest: Double) {
        assertThat(strategy.calculateMonthlyInterest(deposit)).isEqualTo(expectedInterest)
    }

    companion object {
        @JvmStatic
        fun interestProvider(): List<Arguments> = listOf(
            // 5% / 12, from day 46, no upper limit
            argumentSet("day 45 – no interest yet", premiumTimeDeposit(days = 45, balance = 1200.0), 0.0),
            argumentSet("day 46 – earns interest", premiumTimeDeposit(days = 46, balance = 1200.0), 5.0),
            argumentSet("day 366 – still earns", premiumTimeDeposit(days = 366, balance = 1200.0), 5.0),
            argumentSet("interest is rounded", premiumTimeDeposit(days = 46, balance = 100.0), 0.42),
        )
    }
}

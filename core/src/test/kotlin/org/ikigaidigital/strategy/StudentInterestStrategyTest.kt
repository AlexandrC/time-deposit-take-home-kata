package org.ikigaidigital.strategy

import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.within
import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.fixture.TimeDepositFixtures.studentTimeDeposit
import org.ikigaidigital.model.PlanType
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource

class StudentInterestStrategyTest {

    private val strategy = StudentInterestStrategy()

    @Test
    fun `handles student plan type`() {
        assertThat(strategy.planType).isEqualTo(PlanType.STUDENT)
    }

    @ParameterizedTest
    @MethodSource("interestProvider")
    fun `calculateMonthlyInterest returns monthly interest rounded to cents`(deposit: TimeDeposit, expectedInterest: Double) {
        assertThat(strategy.calculateMonthlyInterest(deposit)).isEqualTo(expectedInterest)
    }

    companion object {
        @JvmStatic
        fun interestProvider(): List<Arguments> = listOf(
            // 3% / 12, from day 31 until day 365
            argumentSet("day 30 – no interest yet", studentTimeDeposit(days = 30, balance = 1200.0), 0.0),
            argumentSet("day 31 – earns interest", studentTimeDeposit(days = 31, balance = 1200.0), 3.0),
            argumentSet("day 365 – last day with interest", studentTimeDeposit(days = 365, balance = 1200.0), 3.0),
            argumentSet("day 366 – interest stops", studentTimeDeposit(days = 366, balance = 1200.0), 0.0),
            argumentSet("interest is not rounded", studentTimeDeposit(days = 31, balance = 100.0), 0.25),
        )
    }
}

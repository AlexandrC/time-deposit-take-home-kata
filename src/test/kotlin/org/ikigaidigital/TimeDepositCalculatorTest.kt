package org.ikigaidigital

import org.assertj.core.api.Assertions.assertThat
import org.ikigaidigital.fixture.TimeDepositFixtures.basicTimeDeposit
import org.ikigaidigital.fixture.TimeDepositFixtures.premiumTimeDeposit
import org.ikigaidigital.fixture.TimeDepositFixtures.studentTimeDeposit
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.Arguments.argumentSet
import org.junit.jupiter.params.provider.MethodSource

class TimeDepositCalculatorTest {

    private val calculator = TimeDepositCalculator()


    @ParameterizedTest
    @MethodSource("interestRateProvider")
    fun `updateBalance adds one month of interest`(timeDeposit: TimeDeposit, expectedBalance: Double) {
        val plans = listOf(
            timeDeposit
        )
        calculator.updateBalance(plans)

        assertThat(timeDeposit.balance).isEqualTo(expectedBalance)
    }

    @Test
    fun `updateBalance updates each deposit in the list independently`() {
        val deposits = listOf(
            basicTimeDeposit(days = 31, balance = 1000.0),
            studentTimeDeposit(days = 366, balance = 1000.0),
            premiumTimeDeposit(days = 46, balance = 1000.0),
        )

        calculator.updateBalance(deposits)

        assertThat(deposits.map { it.balance }).containsExactly(1000.83, 1000.0, 1004.17)
    }


    companion object {
        @JvmStatic
        fun interestRateProvider(): List<Arguments> {
            return listOf(
                // Basic: 1% / 12, from day 31, no upper limit
                argumentSet("basic – day 30, no interest yet", basicTimeDeposit(days = 30), 100.00),
                argumentSet("basic – day 31, earns interest", basicTimeDeposit(days = 31), 100.08),
                argumentSet("basic – day 366, still earns", basicTimeDeposit(days = 366), 100.08),
                argumentSet("basic – large balance", basicTimeDeposit(days = 45, balance = 1234567.00), 1235595.81),

                // Student: 3% / 12, from day 31 until day 365
                argumentSet("student – day 30, no interest yet", studentTimeDeposit(days = 30), 100.00),
                argumentSet("student – day 31, earns interest", studentTimeDeposit(days = 31), 100.25),
                argumentSet("student – day 365, last day with interest", studentTimeDeposit(days = 365), 100.25),
                argumentSet("student – day 366, interest stops", studentTimeDeposit(days = 366), 100.00),

                // Premium: 5% / 12, from day 46, no upper limit
                argumentSet("premium – day 45, no interest yet", premiumTimeDeposit(days = 45), 100.00),
                argumentSet("premium – day 46, earns interest", premiumTimeDeposit(days = 46), 100.42),
                argumentSet("premium – day 366, still earns", premiumTimeDeposit(days = 366), 100.42),

                // Plan type must match exactly
                argumentSet("unknown plan – no interest", TimeDeposit(12, "gold", 100.00, 100), 100.00),
                argumentSet("wrong case – no interest", TimeDeposit(13, "Basic", 100.00, 100), 100.00),

                // Rounding: HALF_UP on the raw double value
                argumentSet("under half a cent – dropped", basicTimeDeposit(days = 31, balance = 0.60), 0.60),
                argumentSet("0.015 stored as 0.01499… – rounds down", basicTimeDeposit(days = 31, balance = 18.00), 18.01),
                argumentSet("0.005 stored as 0.00500…01 – rounds up", basicTimeDeposit(days = 31, balance = 6.00), 6.01),
            )
        }
    }
}


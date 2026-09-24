package org.ikigaidigital

import org.ikigaidigital.model.PlanType
import org.ikigaidigital.strategy.BasicInterestStrategy
import org.ikigaidigital.strategy.InterestStrategy
import org.ikigaidigital.strategy.PremiumInterestStrategy
import org.ikigaidigital.strategy.StudentInterestStrategy
import java.math.BigDecimal
import java.math.RoundingMode

class TimeDepositCalculator(
    strategies: List<InterestStrategy> = listOf(
        BasicInterestStrategy(),
        StudentInterestStrategy(),
        PremiumInterestStrategy(),
    ),
) {
    private val strategies: Map<PlanType, InterestStrategy> = strategies
        .associateBy(InterestStrategy::planType)

    fun updateBalance(xs: List<TimeDeposit>) {
        for (deposit in xs) {
            val interest = provide(deposit.planType)?.calculateMonthlyInterest(deposit) ?: 0.0
            deposit.balance += BigDecimal(interest).setScale(2, RoundingMode.HALF_UP).toDouble()
        }
    }

    /** Unknown plan types (e.g. "gold", "Basic") earn no interest – legacy behaviour. */
    private fun provide(planType: String): InterestStrategy? =
        PlanType.fromCode(planType)?.let { strategies[it] }
}

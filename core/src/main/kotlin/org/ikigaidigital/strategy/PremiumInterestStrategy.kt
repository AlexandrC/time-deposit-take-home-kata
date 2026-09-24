package org.ikigaidigital.strategy

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.PlanType

class PremiumInterestStrategy : InterestStrategy {

    override fun calculateMonthlyInterest(deposit: TimeDeposit): Double =
        if (deposit.days > INTEREST_STARTS_AFTER_DAYS) {
            deposit.balance * ANNUAL_RATE / MONTHS_PER_YEAR
        } else {
            0.0
        }

    override val planType: PlanType = PlanType.PREMIUM

    private companion object {
        const val ANNUAL_RATE = 0.05
        /** Interest starts after 45 days – also covers the general 30-day grace period. */
        const val INTEREST_STARTS_AFTER_DAYS = 45
    }
}

package org.ikigaidigital.strategy

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.PlanType

class BasicInterestStrategy : InterestStrategy {

    override fun calculateMonthlyInterest(deposit: TimeDeposit): Double =
        if (deposit.days > GRACE_PERIOD_DAYS) {
            (deposit.balance * ANNUAL_RATE / MONTHS_PER_YEAR).roundToCents()
        } else {
            0.0
        }

    override val planType: PlanType = PlanType.BASIC

    private companion object {
        const val ANNUAL_RATE = 0.01
        const val GRACE_PERIOD_DAYS = 30
    }
}

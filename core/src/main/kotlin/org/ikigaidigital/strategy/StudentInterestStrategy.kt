package org.ikigaidigital.strategy

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.PlanType

class StudentInterestStrategy : InterestStrategy {

    override fun calculateMonthlyInterest(deposit: TimeDeposit): Double =
        if (deposit.days in FIRST_DAY_WITH_INTEREST..LAST_DAY_WITH_INTEREST) {
            deposit.balance * ANNUAL_RATE / MONTHS_PER_YEAR
        } else {
            0.0
        }

    override val planType: PlanType = PlanType.STUDENT

    private companion object {
        const val ANNUAL_RATE = 0.03
        const val FIRST_DAY_WITH_INTEREST = 31
        /** "No interest after 1 year" – legacy check was `days < 366`. */
        const val LAST_DAY_WITH_INTEREST = 365
    }
}

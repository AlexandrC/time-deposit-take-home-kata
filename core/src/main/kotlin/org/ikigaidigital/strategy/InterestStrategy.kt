package org.ikigaidigital.strategy

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.PlanType

/**
 * Monthly interest rule for one plan type, including its own eligibility rules
 * (grace period, age limits). Returns the raw interest; rounding stays in TimeDepositCalculator.
 */
interface InterestStrategy {

    fun calculateMonthlyInterest(deposit: TimeDeposit): Double

    val planType: PlanType
}

internal const val MONTHS_PER_YEAR = 12

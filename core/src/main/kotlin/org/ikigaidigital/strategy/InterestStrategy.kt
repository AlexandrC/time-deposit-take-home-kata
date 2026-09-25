package org.ikigaidigital.strategy

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.PlanType
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * Monthly interest rule for one plan type, including its own eligibility rules
 * (grace period, age limits). Returns the interest already rounded to cents."
 */
interface InterestStrategy {

    fun calculateMonthlyInterest(deposit: TimeDeposit): Double

    val planType: PlanType
}

internal const val MONTHS_PER_YEAR = 12

/** Rounds to whole cents, HALF_UP on the raw double value – legacy behaviour. */
internal fun Double.roundToCents(): Double =
    BigDecimal(this).setScale(2, RoundingMode.HALF_UP).toDouble()

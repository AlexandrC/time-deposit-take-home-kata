package org.ikigaidigital.model

import java.math.BigDecimal
import java.time.LocalDate

/**
 * A single withdrawal made from a time deposit.
 *
 * `timeDepositId` from the `withdrawals` table is intentionally omitted:
 * withdrawals are only exposed nested inside their [TimeDepositDetail].
 */
data class Withdrawal(
    val id: Int,
    val amount: BigDecimal,
    val date: LocalDate,
)

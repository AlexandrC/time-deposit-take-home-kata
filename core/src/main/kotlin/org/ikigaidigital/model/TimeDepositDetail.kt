package org.ikigaidigital.model

import java.math.BigDecimal

/**
 * Read model of a time deposit together with its withdrawals.
 */
data class TimeDepositDetail(
    val id: Int,
    val planType: String,
    val balance: BigDecimal,
    val days: Int,
    val withdrawals: List<Withdrawal>,
)

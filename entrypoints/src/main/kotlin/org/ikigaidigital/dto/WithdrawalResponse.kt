package org.ikigaidigital.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal
import java.time.LocalDate

@Schema(description = "A single withdrawal from a time deposit")
data class WithdrawalResponse(
    @field:Schema(description = "Withdrawal identifier", example = "1")
    val id: Int,

    @field:Schema(description = "Withdrawn amount", example = "100.00")
    val amount: BigDecimal,

    @field:Schema(description = "Date of the withdrawal", example = "2026-01-15")
    val date: LocalDate,
)

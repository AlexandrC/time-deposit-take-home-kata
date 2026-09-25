package org.ikigaidigital.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.math.BigDecimal

@Schema(description = "A time deposit with its withdrawal history")
data class TimeDepositResponse(
    @field:Schema(description = "Time deposit identifier", example = "1")
    val id: Int,

    @field:Schema(
        description = "Interest plan of the deposit",
        example = "premium",
        allowableValues = ["basic", "student", "premium"],
    )
    val planType: String,

    @field:Schema(description = "Current balance", example = "1234.56")
    val balance: BigDecimal,

    @field:Schema(description = "Age of the deposit in days", example = "60")
    val days: Int,

    @field:Schema(description = "Withdrawals made from the deposit, oldest first")
    val withdrawals: List<WithdrawalResponse>,
)

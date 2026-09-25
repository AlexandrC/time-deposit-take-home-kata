package org.ikigaidigital.mapper

import org.ikigaidigital.dto.TimeDepositResponse
import org.ikigaidigital.dto.WithdrawalResponse
import org.ikigaidigital.model.TimeDepositDetail
import org.ikigaidigital.model.Withdrawal

internal fun TimeDepositDetail.toResponse() = TimeDepositResponse(
    id = id,
    planType = planType,
    balance = balance,
    days = days,
    withdrawals = withdrawals.map { it.toResponse() },
)

internal fun Withdrawal.toResponse() = WithdrawalResponse(
    id = id,
    amount = amount,
    date = date,
)

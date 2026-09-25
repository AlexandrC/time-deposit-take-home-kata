package org.ikigaidigital.mapper

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.entity.TimeDepositEntity
import org.ikigaidigital.entity.WithdrawalEntity
import org.ikigaidigital.model.TimeDepositDetail
import org.ikigaidigital.model.Withdrawal
import java.math.BigDecimal
import java.math.RoundingMode

internal fun TimeDepositEntity.toDomain() =
    TimeDeposit(id = id, planType = planType, balance = balance.toDouble(), days = days)

internal fun TimeDepositEntity.toDetail() =
    TimeDepositDetail(id, planType, balance, days, withdrawals.map { it.toDomain() })

internal fun WithdrawalEntity.toDomain() = Withdrawal(id, amount, date)

/** The calculator works on Double; the DB stores NUMERIC(19,2). */
internal fun Double.toMoney(): BigDecimal = BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP)

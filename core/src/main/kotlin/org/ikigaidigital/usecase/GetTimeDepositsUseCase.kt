package org.ikigaidigital.usecase

import org.ikigaidigital.model.TimeDepositDetail

/**
 * Returns all stored time deposits including their withdrawals.
 */
interface GetTimeDepositsUseCase {

    fun getAllTimeDeposits(): List<TimeDepositDetail>
}

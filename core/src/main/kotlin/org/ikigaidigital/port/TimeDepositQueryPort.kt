package org.ikigaidigital.port

import org.ikigaidigital.model.TimeDepositDetail

/**
 * Read-side persistence port: loads time deposits together with their withdrawals.
 */
interface TimeDepositQueryPort {

    fun findAllWithWithdrawals(): List<TimeDepositDetail>
}

package org.ikigaidigital.port

import org.ikigaidigital.TimeDeposit

/**
 * Write-side persistence port: loads and stores time deposits
 * in the shape the [org.ikigaidigital.TimeDepositCalculator] works with.
 */
interface TimeDepositRepositoryPort {

    fun findAll(): List<TimeDeposit>

    fun saveAll(timeDeposits: List<TimeDeposit>)
}

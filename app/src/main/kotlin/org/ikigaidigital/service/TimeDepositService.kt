package org.ikigaidigital.service

import org.ikigaidigital.TimeDepositCalculator
import org.ikigaidigital.port.TimeDepositQueryPort
import org.ikigaidigital.port.TimeDepositRepositoryPort
import org.ikigaidigital.usecase.GetTimeDepositsUseCase
import org.ikigaidigital.usecase.UpdateBalancesUseCase
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class TimeDepositService(
    private val repository: TimeDepositRepositoryPort,
    private val queryPort: TimeDepositQueryPort,
    private val calculator: TimeDepositCalculator = TimeDepositCalculator(),
) : UpdateBalancesUseCase, GetTimeDepositsUseCase {

    @Transactional
    override fun updateAllBalances() {
        val deposits = repository.findAll()
        calculator.updateBalance(deposits)
        repository.saveAll(deposits)
    }

    @Transactional(readOnly = true)
    override fun getAllTimeDeposits() = queryPort.findAllWithWithdrawals()
}

package org.ikigaidigital.adapter

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.mapper.toDomain
import org.ikigaidigital.mapper.toMoney
import org.ikigaidigital.port.TimeDepositRepositoryPort
import org.ikigaidigital.repository.TimeDepositJpaRepository
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class TimeDepositRepositoryAdapter(
    private val jpaRepository: TimeDepositJpaRepository,
) : TimeDepositRepositoryPort {

    override fun findAll(): List<TimeDeposit> =
        jpaRepository.findAll(Sort.by("id")).map { it.toDomain() }

    /** Updates balances only; deposits are never created or otherwise modified here. */
    override fun saveAll(timeDeposits: List<TimeDeposit>) {
        val entities = jpaRepository.findAllById(timeDeposits.map { it.id }).associateBy { it.id }
        timeDeposits.forEach { deposit ->
            val entity = entities[deposit.id] ?: error("Time deposit ${deposit.id} not found")
            entity.balance = deposit.balance.toMoney()
        }
        jpaRepository.saveAll(entities.values)
    }
}

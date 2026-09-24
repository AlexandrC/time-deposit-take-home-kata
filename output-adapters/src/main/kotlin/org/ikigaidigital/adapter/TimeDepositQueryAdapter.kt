package org.ikigaidigital.adapter

import org.ikigaidigital.mapper.toDetail
import org.ikigaidigital.model.TimeDepositDetail
import org.ikigaidigital.port.TimeDepositQueryPort
import org.ikigaidigital.repository.TimeDepositJpaRepository
import org.springframework.stereotype.Component

@Component
class TimeDepositQueryAdapter(
    private val jpaRepository: TimeDepositJpaRepository,
) : TimeDepositQueryPort {

    override fun findAllWithWithdrawals(): List<TimeDepositDetail> =
        jpaRepository.findAllWithWithdrawals().map { it.toDetail() }
}

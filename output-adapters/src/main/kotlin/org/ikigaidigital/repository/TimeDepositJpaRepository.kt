package org.ikigaidigital.repository

import org.ikigaidigital.entity.TimeDepositEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface TimeDepositJpaRepository : JpaRepository<TimeDepositEntity, Int> {

    // Single query with a fetch join, so no N+1 when loading withdrawals.
    @Query("""
        select d from TimeDepositEntity d
        left join fetch d.withdrawals w
        order by d.id, w.date, w.id
    """)
    fun findAllWithWithdrawals(): List<TimeDepositEntity>
}

package org.ikigaidigital.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "time_deposits")
class TimeDepositEntity(
    // No @GeneratedValue: this service never creates deposits, it only reads and updates them.
    @Id
    var id: Int,

    @Column(name = "plan_type", nullable = false)
    var planType: String,

    @Column(nullable = false)
    var days: Int,

    @Column(nullable = false, precision = 19, scale = 2)
    var balance: BigDecimal,

    // Unidirectional and read-only: withdrawals are history this service never writes.
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "time_deposit_id", nullable = false, insertable = false, updatable = false)
    var withdrawals: MutableList<WithdrawalEntity> = mutableListOf(),
)

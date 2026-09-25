package org.ikigaidigital.fixture

import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.PlanType

object TimeDepositFixtures {

    fun basicTimeDeposit(
        id: Int = 1,
        days: Int = 1,
        balance: Double = 100.00
    ) = TimeDeposit(
            id = id,
            planType = PlanType.BASIC.code,
            days = days,
            balance = balance
        )
    fun studentTimeDeposit(
        id: Int = 1,
        days: Int = 1,
        balance: Double = 100.00
    ) = TimeDeposit(
            id = id,
            planType = PlanType.STUDENT.code,
            days = days,
            balance = balance
        )
    fun premiumTimeDeposit(
        id: Int = 1,
        days: Int = 1,
        balance: Double = 100.00
    ) = TimeDeposit(
            id = id,
            planType = PlanType.PREMIUM.code,
            days = days,
            balance = balance
        )
}

package org.ikigaidigital.fixture

import org.ikigaidigital.TimeDeposit

object TimeDepositFixtures {

    fun basicTimeDeposit(
        id: Int = 1,
        days: Int = 1,
        balance: Double = 100.00
    ) = TimeDeposit(
            id = id,
            planType = "basic",
            days = days,
            balance = balance
        )
    fun studentTimeDeposit(
        id: Int = 1,
        days: Int = 1,
        balance: Double = 100.00
    ) = TimeDeposit(
            id = id,
            planType = "student",
            days = days,
            balance = balance
        )
    fun premiumTimeDeposit(
        id: Int = 1,
        days: Int = 1,
        balance: Double = 100.00
    ) = TimeDeposit(
            id = id,
            planType = "premium",
            days = days,
            balance = balance
        )
}

package org.ikigaidigital.usecase

/**
 * Assumption: `days` (deposit age) is maintained outside this service, e.g. by a daily
 * batch in the core banking system. This use case only applies one month of interest
 * based on the current age and does not advance `days` itself.
 */
interface UpdateBalancesUseCase {

    fun updateAllBalances()
}

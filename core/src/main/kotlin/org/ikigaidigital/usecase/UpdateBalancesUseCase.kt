package org.ikigaidigital.usecase

/**
 * Applies one month of interest to the balance of every stored time deposit.
 */
interface UpdateBalancesUseCase {

    fun updateAllBalances()
}

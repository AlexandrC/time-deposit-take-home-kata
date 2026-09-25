package org.ikigaidigital.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.ikigaidigital.dto.TimeDepositResponse
import org.ikigaidigital.mapper.toResponse
import org.ikigaidigital.usecase.GetTimeDepositsUseCase
import org.ikigaidigital.usecase.UpdateBalancesUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/time-deposits")
@Tag(name = "Time deposits", description = "Read time deposits and apply monthly interest")
class TimeDepositController(
    private val getTimeDepositsUseCase: GetTimeDepositsUseCase,
    private val updateBalancesUseCase: UpdateBalancesUseCase,
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    @Operation(
        summary = "Get all time deposits",
        description = """
            Returns every stored time deposit with its plan type, current balance,
            age in days and the list of withdrawals (oldest first).
        """,
    )
    @ApiResponse(responseCode = "200", description = "List of time deposits (empty if none exist)")
    fun getAllTimeDeposits(): List<TimeDepositResponse> =
        getTimeDepositsUseCase.getAllTimeDeposits().map { it.toResponse() }


    @PostMapping("/balance-updates")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(
        summary = "Update balances of all time deposits",
        description = """
        Runs the monthly interest calculation on every stored time deposit and saves the new balances.
        Annual rates, applied as 1/12 per call: basic 1%, student 3% (only while days < 366),
        premium 5% (only when days > 45). No interest while days <= 30.
        Each call applies interest again (not idempotent).
    """,
    )
    @ApiResponse(responseCode = "204", description = "Balances updated")
    fun updateAllBalances() {
        updateBalancesUseCase.updateAllBalances()
    }
}

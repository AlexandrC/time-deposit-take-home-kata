package org.ikigaidigital.controller

import org.ikigaidigital.model.TimeDepositDetail
import org.ikigaidigital.model.Withdrawal
import org.ikigaidigital.usecase.GetTimeDepositsUseCase
import org.ikigaidigital.usecase.UpdateBalancesUseCase
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import java.time.LocalDate

@WebMvcTest(TimeDepositController::class)
class TimeDepositControllerTest(
    @Autowired private val mockMvc: MockMvc,
) {

    @MockitoBean
    private lateinit var getTimeDeposits: GetTimeDepositsUseCase

    @MockitoBean
    private lateinit var updateBalances: UpdateBalancesUseCase

    @Test
    fun `GET returns all time deposits with withdrawals`() {
        given(getTimeDeposits.getAllTimeDeposits()).willReturn(
            listOf(
                TimeDepositDetail(
                    id = 1,
                    planType = "basic",
                    balance = "1234.56".toBigDecimal(),
                    days = 45,
                    withdrawals = listOf(Withdrawal(7, "100.00".toBigDecimal(), LocalDate.of(2026, 1, 15))),
                ),
            ),
        )

        mockMvc.get("/time-deposits").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(1) }
            jsonPath("$[0].id") { value(1) }
            jsonPath("$[0].planType") { value("basic") }
            jsonPath("$[0].balance") { value(1234.56) }
            jsonPath("$[0].days") { value(45) }
            jsonPath("$[0].withdrawals[0].id") { value(7) }
            jsonPath("$[0].withdrawals[0].amount") { value(100.0) }
            jsonPath("$[0].withdrawals[0].date") { value("2026-01-15") }
        }
    }

    @Test
    fun `GET returns empty list when there are no deposits`() {
        given(getTimeDeposits.getAllTimeDeposits()).willReturn(emptyList())

        mockMvc.get("/time-deposits").andExpect {
            status { isOk() }
            content { json("[]") }
        }
    }

    @Test
    fun `POST balance-updates triggers the update and returns 204`() {
        mockMvc.post("/time-deposits/balance-updates").andExpect {
            status { isNoContent() }
            content { string("") }
        }

        verify(updateBalances).updateAllBalances()
    }
}

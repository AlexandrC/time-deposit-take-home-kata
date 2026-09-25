package org.ikigaidigital.integrationTest

import org.ikigaidigital.config.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@Sql("/sql/time-deposits.sql")
class TimeDepositApiIntegrationTest : IntegrationTest() {

    @Test
    fun `GET returns stored deposits with withdrawals`() {
        mockMvc.get("/time-deposits").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(6) }
            jsonPath("$[0].planType") { value("basic") }
            jsonPath("$[0].balance") { value(1000.0) }
            jsonPath("$[0].withdrawals[0].amount") { value(50.0) }
            jsonPath("$[0].withdrawals[0].date") { value("2026-01-15") }
            jsonPath("$[1].withdrawals.length()") { value(0) }
        }
    }

    @Test
    fun `POST applies monthly interest and persists new balances`() {
        mockMvc.post("/time-deposits/balance-updates").andExpect {
            status { isNoContent() }
        }

        mockMvc.get("/time-deposits").andExpect {
            status { isOk() }
            jsonPath("$[0].balance") { value(1000.83) }  // basic, gets interest
            jsonPath("$[1].balance") { value(1000.0) }   // basic, first 30 days
            jsonPath("$[2].balance") { value(2005.0) }   // student, gets interest
            jsonPath("$[3].balance") { value(2000.0) }   // student, older than 1 year
            jsonPath("$[4].balance") { value(1200.0) }   // premium, before day 45
            jsonPath("$[5].balance") { value(1205.0) }   // premium, gets interest
        }
    }

    @Test
    @Sql(statements = ["TRUNCATE withdrawals, time_deposits"]) // method-level @Sql replaces the class-level seed
    fun `POST on empty database returns 204`() {
        mockMvc.post("/time-deposits/balance-updates").andExpect {
            status { isNoContent() }
        }

        mockMvc.get("/time-deposits").andExpect {
            status { isOk() }
            jsonPath("$.length()") { value(0) }
        }
    }
}

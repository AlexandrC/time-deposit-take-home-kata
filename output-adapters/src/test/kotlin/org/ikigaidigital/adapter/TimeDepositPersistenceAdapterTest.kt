package org.ikigaidigital.adapter

import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.ikigaidigital.PostgresTestcontainersConfig
import org.ikigaidigital.TimeDeposit
import org.ikigaidigital.model.Withdrawal
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase
import org.springframework.context.annotation.Import
import org.springframework.test.context.jdbc.Sql
import java.math.BigDecimal
import java.time.LocalDate

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(PostgresTestcontainersConfig::class, TimeDepositRepositoryAdapter::class, TimeDepositQueryAdapter::class)
@Sql("/sql/time-deposits.sql")
class TimeDepositPersistenceAdapterTest {

    @Autowired
    lateinit var repositoryAdapter: TimeDepositRepositoryAdapter

    @Autowired
    lateinit var queryAdapter: TimeDepositQueryAdapter

    @PersistenceContext
    lateinit var entityManager: EntityManager

    @Test
    fun `findAll maps entities to TimeDeposit ordered by id`() {
        assertThat(repositoryAdapter.findAll()).containsExactly(
            TimeDeposit(1, "basic", 1000.0, 45),
            TimeDeposit(2, "student", 2000.0, 400),
            TimeDeposit(3, "premium", 1200.0, 60),
        )
    }

    @Test
    fun `saveAll persists balance rounded to 2 decimals and nothing else`() {
        val deposits = repositoryAdapter.findAll()
        deposits.first { it.id == 1 }.balance = 1000.8333333

        repositoryAdapter.saveAll(deposits)
        entityManager.flush()
        entityManager.clear()

        assertThat(repositoryAdapter.findAll()).containsExactly(
            TimeDeposit(1, "basic", 1000.83, 45),
            TimeDeposit(2, "student", 2000.0, 400),
            TimeDeposit(3, "premium", 1200.0, 60),
        )
    }

    @Test
    fun `saveAll fails for unknown deposit`() {
        assertThatThrownBy { repositoryAdapter.saveAll(listOf(TimeDeposit(99, "basic", 1.0, 1))) }
            .isInstanceOf(IllegalStateException::class.java)
            .hasMessageContaining("99")
    }

    @Test
    fun `findAllWithWithdrawals returns deposits with withdrawals ordered by date`() {
        val result = queryAdapter.findAllWithWithdrawals()

        assertThat(result.map { it.id }).containsExactly(1, 2, 3)
        assertThat(result[0].balance).isEqualByComparingTo("1000.00")
        assertThat(result[0].withdrawals).containsExactly(
            Withdrawal(2, BigDecimal("100.00"), LocalDate.parse("2026-01-15")),
            Withdrawal(1, BigDecimal("50.00"), LocalDate.parse("2026-02-10")),
        )
        assertThat(result[1].withdrawals).isEmpty()
        assertThat(result[2].withdrawals).isEmpty()
    }
}

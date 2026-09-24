package org.ikigaidigital.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.context.annotation.Import
import org.springframework.test.web.servlet.MockMvc

/**
 * Base class for full-stack tests: HTTP → service → JPA → real PostgreSQL (Testcontainers).
 * All subclasses share one Spring context and one container.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestcontainersConfig::class)
abstract class IntegrationTest {

    @Autowired
    protected lateinit var mockMvc: MockMvc
}

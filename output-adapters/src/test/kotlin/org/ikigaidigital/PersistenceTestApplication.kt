package org.ikigaidigital

import org.springframework.boot.SpringBootConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration

/**
 * @DataJpaTest needs a @SpringBootConfiguration and this module has no application class.
 * @EnableAutoConfiguration also registers `org.ikigaidigital` for entity and repository scanning.
 */
@SpringBootConfiguration
@EnableAutoConfiguration
class PersistenceTestApplication

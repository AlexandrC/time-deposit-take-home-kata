package org.ikigaidigital.config

import org.ikigaidigital.TimeDepositApplication
import org.springframework.boot.fromApplication
import org.springframework.boot.with

/**
 * Runs the app against a throwaway Postgres container: `./gradlew :bootstrap:bootTestRun`.
 */
fun main(args: Array<String>) {
    fromApplication<TimeDepositApplication>()
        .with(PostgresTestcontainersConfig::class)
        .run(*args)
}

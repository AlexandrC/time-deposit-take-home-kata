package org.ikigaidigital.model

/**
 * Interest plans a time deposit can have.
 * [code] is the value stored in the database and in TimeDeposit.planType.
 */
enum class PlanType(val code: String) {
    BASIC("basic"),
    STUDENT("student"),
    PREMIUM("premium");

    companion object {
        /**
         * Exact, case-sensitive lookup. Returns null for unknown codes (e.g. "gold", "Basic"),
         * because the legacy calculator gives such deposits no interest instead of failing.
         */
        fun fromCode(code: String): PlanType? = entries.find { it.code == code }
    }
}

package com.zukesito.tenge.data.model

/** Define cómo cambia el saldo al confirmarlo */
enum class NoticeSource {
    // Aumenta el saldo
    RECURRING_INCOME,
    // Reduce el saldo
    RECURRING_EXPENSE,
    // Reduce el saldo y suma al acumulado del plan
    SAVINGS_QUOTA
}
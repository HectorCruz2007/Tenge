package com.zukesito.tenge.presentation.labels

import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import com.zukesito.tenge.R
import com.zukesito.tenge.core.finance.RiskLevel
import com.zukesito.tenge.core.finance.SavingsStatus
import com.zukesito.tenge.data.model.Frequency
import com.zukesito.tenge.data.model.NoticeSource

// Textos visibles de los enums, definidos en strings.xml

@StringRes
fun RiskLevel.labelRes(): Int = when (this) {
    RiskLevel.LOW -> R.string.risk_low
    RiskLevel.MEDIUM -> R.string.risk_medium
    RiskLevel.HIGH -> R.string.risk_high
    RiskLevel.CRITICAL -> R.string.risk_critical
}

@StringRes
fun SavingsStatus.labelRes(): Int = when (this) {
    SavingsStatus.IN_PROGRESS -> R.string.savings_status_in_progress
    SavingsStatus.BEHIND -> R.string.savings_status_behind
    SavingsStatus.COMPLETED -> R.string.savings_status_completed
}

// "Diario", "Semanal", "Mensual"
@StringRes
fun Frequency.labelRes(): Int = when (this) {
    Frequency.DAILY -> R.string.frequency_daily
    Frequency.WEEKLY -> R.string.frequency_weekly
    Frequency.MONTHLY -> R.string.frequency_monthly
}

// "Diaria", "Semanal", "Mensual" para concordar con "frecuencia"
@StringRes
fun Frequency.feminineLabelRes(): Int = when (this) {
    Frequency.DAILY -> R.string.frequency_daily_f
    Frequency.WEEKLY -> R.string.frequency_weekly
    Frequency.MONTHLY -> R.string.frequency_monthly
}

// "diario", "semanal", "mensual" para frases como "0.8 % mensual"
@StringRes
fun Frequency.adjectiveRes(): Int = when (this) {
    Frequency.DAILY -> R.string.frequency_daily_lower
    Frequency.WEEKLY -> R.string.frequency_weekly_lower
    Frequency.MONTHLY -> R.string.frequency_monthly_lower
}

// "%1$s / día", "%1$s / semana", "%1$s / mes"
@StringRes
fun Frequency.perPeriodRes(): Int = when (this) {
    Frequency.DAILY -> R.string.per_day
    Frequency.WEEKLY -> R.string.per_week
    Frequency.MONTHLY -> R.string.per_month
}

// Unidad del plazo para el selector ("Días", "Semanas", "Meses")
@StringRes
fun Frequency.unitRes(): Int = when (this) {
    Frequency.DAILY -> R.string.unit_days
    Frequency.WEEKLY -> R.string.unit_weeks
    Frequency.MONTHLY -> R.string.unit_months
}

@StringRes
fun NoticeSource.labelRes(): Int = when (this) {
    NoticeSource.RECURRING_INCOME -> R.string.notice_source_income
    NoticeSource.RECURRING_EXPENSE -> R.string.notice_source_expense
    NoticeSource.SAVINGS_QUOTA -> R.string.notice_source_savings
}

// Cantidad de periodos con plural correcto ("1 mes", "6 meses")
@PluralsRes
fun Frequency.periodPluralRes(): Int = when (this) {
    Frequency.DAILY -> R.plurals.period_days
    Frequency.WEEKLY -> R.plurals.period_weeks
    Frequency.MONTHLY -> R.plurals.period_months
}

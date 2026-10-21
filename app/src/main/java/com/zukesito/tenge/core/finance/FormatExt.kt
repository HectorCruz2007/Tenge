package com.zukesito.tenge.core.finance

import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.format.TextStyle
import java.util.Locale

// Formato regional de México
private val MX_LOCALE: Locale = Locale.forLanguageTag("es-MX")
private val MX_DATE: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(MX_LOCALE)
private val MX_SHORT_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("d MMM", MX_LOCALE)
private val MX_MONTH_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", MX_LOCALE)
private val MX_SHORT_MONTH_YEAR: DateTimeFormatter = DateTimeFormatter.ofPattern("MMM yyyy", MX_LOCALE)

/** Centavos a moneda MXN */
fun Long.toMxn(): String =
    NumberFormat.getCurrencyInstance(MX_LOCALE).format(BigDecimal.valueOf(this, 2))

/** Fecha en formato de México */
fun LocalDate.toMxDate(): String = format(MX_DATE)

/** Porcentaje con hasta 2 decimales */
fun Double.toPercentText(): String =
    NumberFormat.getNumberInstance(MX_LOCALE)
        .apply { maximumFractionDigits = 2 }
        .format(this) + " %"

/**
 * Texto capturado a centavos, regresa null si no es número o tiene más de 2 decimales
 */
fun String.toCentsOrNull(): Long? {
    val value = trim().removePrefix("$").replace(",", "").toBigDecimalOrNull() ?: return null
    if (value.scale() > 2) return null
    return runCatching { value.movePointRight(2).longValueExact() }.getOrNull()
}

/** Monto con signo, ej. -84550 → "−$845.50", 200000 → "+$2,000.00". */
fun Long.toSignedMxn(): String = when {
    this < 0 -> "\u2212" + (-this).toMxn()
    this > 0 -> "+" + toMxn()
    else -> toMxn()
}

/** Centavos a MXN sin decimales, ej. 210000 → "$2,100". */
fun Long.toMxnWhole(): String =
    NumberFormat.getCurrencyInstance(MX_LOCALE)
        .apply { maximumFractionDigits = 0 }
        .format(BigDecimal.valueOf(this, 2))

/** Fecha corta sin año, ej. "10 oct". */
fun LocalDate.toMxShortDate(): String = format(MX_SHORT_DATE).replace(".", "")

/** Mes y año con mayúscula inicial, ej. "Octubre 2026". */
fun YearMonth.toMxMonthYear(): String =
    format(MX_MONTH_YEAR).replaceFirstChar { it.titlecase(MX_LOCALE) }

/** Mes abreviado y año, ej. "Nov 2026". */
fun LocalDate.toMxShortMonthYear(): String =
    format(MX_SHORT_MONTH_YEAR).replace(".", "").replaceFirstChar { it.titlecase(MX_LOCALE) }

/** Nombre del día de la semana, ej. "lunes". */
fun LocalDate.toMxWeekdayName(): String = dayOfWeek.getDisplayName(TextStyle.FULL, MX_LOCALE)

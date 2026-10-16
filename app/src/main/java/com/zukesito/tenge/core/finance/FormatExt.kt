package com.zukesito.tenge.core.finance

import java.math.BigDecimal
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

// Formato regional de México
private val MX_LOCALE: Locale = Locale.forLanguageTag("es-MX")
private val MX_DATE: DateTimeFormatter =
    DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM).withLocale(MX_LOCALE)

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
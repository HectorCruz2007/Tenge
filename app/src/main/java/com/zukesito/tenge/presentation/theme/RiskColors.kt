package com.zukesito.tenge.presentation.theme

import androidx.compose.ui.graphics.Color
import com.zukesito.tenge.core.finance.RiskLevel

/** Color dinámico según el nivel de riesgo. */
fun RiskLevel.color(): Color = when (this) {
    RiskLevel.LOW -> RiskLow
    RiskLevel.MEDIUM -> RiskMedium
    RiskLevel.HIGH -> RiskHigh
    RiskLevel.CRITICAL -> RiskCritical
}

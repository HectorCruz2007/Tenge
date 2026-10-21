package com.zukesito.tenge.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.unit.dp

/** Íconos de la app (trazos de Material Icons) sin depender de librerías extra. */
object TengeIcons {
    // NOTA: CONTENIDO GENERADO CON IA PARA ICONOS
    val Home: ImageVector by lazy { icon("home", "M10,20v-6h4v6h5v-8h3L12,3 2,12h3v8z") }
    val Expenses: ImageVector by lazy {
        icon("expenses", "M3,13h2v-2H3v2zM3,17h2v-2H3v2zM3,9h2V7H3v2zM7,13h14v-2H7v2zM7,17h14v-2H7v2zM7,7v2h14V7H7z")
    }
    val Recurring: ImageVector by lazy {
        icon("recurring", "M7,7h10v3l4,-4 -4,-4v3H5v6h2V7zM17,17H7v-3l-4,4 4,4v-3h12v-6h-2v4z")
    }
    val Savings: ImageVector by lazy { icon("savings", "M14.4,6L14,4H5v17h2v-7h5.6l0.4,2h7V6z") }
    val Investments: ImageVector by lazy {
        icon("investments", "M16,6l2.29,2.29 -4.88,4.88 -4,-4L2,16.59 3.41,18l6,-6 4,4 6.3,-6.29L22,12V6z")
    }
    val Add: ImageVector by lazy { icon("add", "M19,13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z") }
    val Back: ImageVector by lazy {
        icon("back", "M20,11H7.83l5.59,-5.59L12,4l-8,8 8,8 1.41,-1.41L7.83,13H20v-2z")
    }
    val Close: ImageVector by lazy {
        icon("close", "M19,6.41L17.59,5 12,10.59 6.41,5 5,6.41 10.59,12 5,17.59 6.41,19 12,13.41 17.59,19 19,17.59 13.41,12z")
    }
    val Error: ImageVector by lazy {
        icon(
            "error",
            "M11,15h2v2h-2zM11,7h2v6h-2zM11.99,2C6.47,2 2,6.48 2,12s4.47,10 9.99,10C17.52,22 22,17.52 22,12S17.52,2 11.99,2zM12,20c-4.42,0 -8,-3.58 -8,-8s3.58,-8 8,-8 8,3.58 8,8 -3.58,8 -8,8z"
        )
    }
    val Calendar: ImageVector by lazy {
        icon(
            "calendar",
            "M17,12h-5v5h5v-5zM16,1v2H8V1H6v2H5c-1.11,0 -1.99,0.9 -1.99,2L3,19c0,1.1 0.89,2 2,2h14c1.1,0 2,-0.9 2,-2V5c0,-1.1 -0.9,-2 -2,-2h-1V1h-2zM19,19H5V8h14v11z"
        )
    }
}

// Construye un ícono de 24 dp a partir de su trazo SVG
private fun icon(name: String, path: String): ImageVector =
    ImageVector.Builder(
        name = name,
        defaultWidth = 24.dp,
        defaultHeight = 24.dp,
        viewportWidth = 24f,
        viewportHeight = 24f
    ).addPath(pathData = addPathNodes(path), fill = SolidColor(Color.Black)).build()

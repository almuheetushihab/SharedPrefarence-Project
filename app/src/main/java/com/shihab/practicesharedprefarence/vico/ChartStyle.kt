package com.shihab.practicesharedprefarence.vico

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.patrykandpatrick.vico.compose.cartesian.style.CartesianChartStyle
import com.patrykandpatrick.vico.compose.cartesian.style.rememberCartesianChartStyle
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.Defaults
import com.patrykandpatrick.vico.core.common.shape.Shape

@Composable
fun rememberChartStyle(chartColors: List<Color>): CartesianChartStyle {
    val isDark = isSystemInDarkTheme()
    return rememberCartesianChartStyle(
        columnLayerColors = chartColors,
        lineLayerColors = chartColors
    )
}

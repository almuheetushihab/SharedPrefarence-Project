package com.shihab.practicesharedprefarence.vico

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart

// In Vico 2.x, styling is often applied directly to layers or via a ProvidableCompositionLocal.
// This helper can be used to create a styled ColumnCartesianLayer.
@Composable
fun rememberStyledColumnLayer(chartColors: List<Color>): ColumnCartesianLayer {
    return rememberColumnCartesianLayer(
        columnProvider = ColumnCartesianLayer.ColumnProvider.series(
            chartColors.map { color ->
                com.patrykandpatrick.vico.core.common.data.CartesianLayerStyle.fromColor(color)
            }
        )
    )
}

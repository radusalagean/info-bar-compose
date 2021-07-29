package com.radusalagean.infobarcompose.sample.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp

@ExperimentalAnimationApi
@Composable
fun <T : GroupConfig> BaseGroup(
    modifier: Modifier = Modifier,
    indicator: @Composable RowScope.(T, Int) -> Unit,
    config: T,
    orientation: Orientation
) {

    @Composable
    fun layOutOptions(
        itemModifier: Modifier = Modifier,
    ) {
        config.options.forEachIndexed { index, currentStringResId ->
            Row(
                modifier = itemModifier
                    .padding(vertical = 4.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        role = Role.RadioButton
                    ) {
                        config.onIndexSelect(index)
                    }
            ) {
                indicator(config, index)
                Text(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    text = stringResource(currentStringResId)
                )
            }
        }
    }

    Column(
        modifier = modifier
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(4.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            text = stringResource(config.groupTitle),
            style = MaterialTheme.typography.body1
        )
        if (orientation == Orientation.HORIZONTAL) {
            Row(Modifier.align(Alignment.CenterHorizontally)) {
                layOutOptions(Modifier.align(Alignment.CenterVertically).weight(1f))
            }
        } else {
            layOutOptions()
        }

    }
}

enum class Orientation {
    HORIZONTAL, VERTICAL
}
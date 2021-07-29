package com.radusalagean.infobarcompose.sample.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.radusalagean.infobarcompose.sample.R

val radioButtonIndicator: @Composable RowScope.(GroupConfig.RadioGroupConfig, Int) -> Unit =
    { config, index ->
        RadioButton(
            modifier = Modifier.align(Alignment.CenterVertically).padding(end = 12.dp),
            selected = index == config.selectedIndex,
            onClick = {
                config.onIndexSelect(index)
            }
        )
    }

@ExperimentalAnimationApi
@Composable
fun RadioGroup(
    modifier: Modifier = Modifier,
    config: GroupConfig.RadioGroupConfig,
    orientation: Orientation = Orientation.VERTICAL
) {
    BaseGroup(
        modifier = modifier,
        indicator = radioButtonIndicator,
        config = config,
        orientation = orientation
    )
}

@ExperimentalAnimationApi
@Preview
@Composable
fun RadioGroupPreview() {
    RadioGroup(
        config = GroupConfig.RadioGroupConfig(
            groupTitle = R.string.radio_group_message_position,
            options = listOf(
                R.string.radio_group_message_position_top,
                R.string.radio_group_message_position_bottom
            )
        ),
        orientation = Orientation.VERTICAL
    )
}
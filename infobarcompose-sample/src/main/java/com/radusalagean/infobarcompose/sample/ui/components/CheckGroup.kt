package com.radusalagean.infobarcompose.sample.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.radusalagean.infobarcompose.sample.R

val checkBoxIndicator: @Composable RowScope.(GroupConfig.CheckGroupConfig, Int) -> Unit =
    { config, index ->
        Checkbox(
            modifier = Modifier.align(Alignment.CenterVertically).padding(end = 12.dp),
            checked = config.selectedIndices[index],
            onCheckedChange = { config.onIndexSelect(index) }
        )
    }

@Composable
fun CheckGroup(
    modifier: Modifier = Modifier,
    config: GroupConfig.CheckGroupConfig,
    orientation: Orientation = Orientation.VERTICAL
) {
    BaseGroup(
        modifier = modifier,
        indicator = checkBoxIndicator,
        config = config,
        orientation = orientation
    )
}

@Preview
@Composable
fun CheckGroupPreview() {
    CheckGroup(
        config = GroupConfig.CheckGroupConfig(
            groupTitle = R.string.check_group_message_animation,
            options = listOf(
                R.string.check_group_message_animation_fade,
                R.string.check_group_message_animation_slide
            ),
            initialIndices = listOf(false, true)
        ),
        orientation = Orientation.VERTICAL
    )
}
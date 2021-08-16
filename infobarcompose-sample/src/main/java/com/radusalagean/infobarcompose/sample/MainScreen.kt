package com.radusalagean.infobarcompose.sample

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radusalagean.infobarcompose.InfoBar
import com.radusalagean.infobarcompose.sample.ui.components.CheckGroup
import com.radusalagean.infobarcompose.sample.ui.components.Orientation
import com.radusalagean.infobarcompose.sample.ui.components.RadioGroup

@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(
            Modifier.fillMaxSize()
        ) {
            val scrollState = rememberScrollState()
            Column(
                Modifier
                    .wrapContentHeight()
                    .align(Alignment.Center)
                    .verticalScroll(scrollState)
                    .padding(vertical = 128.dp, horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.textField,
                    onValueChange = viewModel::onTextValueChange,
                    label = { Text(stringResource(R.string.message_hint)) },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                Row(Modifier.padding(top = 8.dp)) {
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        config = viewModel.messageTimeoutRadioGroup
                    )
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        config = viewModel.messageTypeRadioGroup
                    )
                }
                Row(Modifier.padding(top = 8.dp)) {
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 4.dp),
                        config = viewModel.messagePositionRadioGroup
                    )
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 4.dp),
                        config = viewModel.messageActionRadioGroup
                    )
                }
                CheckGroup(
                    modifier = Modifier
                        .padding(top = 8.dp),
                    config = viewModel.messageAnimationCheckGroup,
                    orientation = Orientation.HORIZONTAL
                )
                Button(
                    modifier = Modifier
                        .padding(top = 32.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = viewModel::onShowMessageClick
                ) {
                    Text(stringResource(R.string.show_message_button))
                }
                AboutSection(
                    Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
            InfoBar( // Generic InfoBar
                modifier = Modifier
                    .padding(16.dp)
                    .align(viewModel.infoBarAlignment),
                offeredMessage = viewModel.infoBarMessage,
                fadeEffect = viewModel.infoBarFadeEffect,
                scaleEffect = viewModel.infoBarScaleEffect,
                slideEffect = viewModel.infoBarSlideEffect,
                onDismiss = viewModel::onInfoBarMessageTimeout
            )
            InfoBar( // Custom InfoBar
                modifier = Modifier
                    .padding(16.dp)
                    .align(viewModel.infoBarAlignment),
                offeredMessage = viewModel.customInfoBarMessage,
                content = customInfoBarContent,
                fadeEffect = viewModel.infoBarFadeEffect,
                scaleEffect = viewModel.infoBarScaleEffect,
                slideEffect = viewModel.infoBarSlideEffect,
                onDismiss = viewModel::onCustomInfoBarMessageTimeout
            )
        }
    }
}

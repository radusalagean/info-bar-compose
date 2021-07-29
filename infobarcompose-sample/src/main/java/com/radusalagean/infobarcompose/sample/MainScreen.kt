package com.radusalagean.infobarcompose.sample

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radusalagean.infobarcompose.InfoBar
import com.radusalagean.infobarcompose.sample.ui.components.CheckGroup
import com.radusalagean.infobarcompose.sample.ui.components.Orientation
import com.radusalagean.infobarcompose.sample.ui.components.RadioGroup

@ExperimentalAnimationApi
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
                    .padding(vertical = 200.dp, horizontal = 16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.textField,
                    onValueChange = viewModel::onTextValueChange,
                    label = { Text(stringResource(R.string.message_hint)) },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
                )
                Row(Modifier.padding(top = 16.dp)) {
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f),
                        config = viewModel.messageTimeoutRadioGroup
                    )
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        config = viewModel.messageTypeRadioGroup
                    )
                }
                Row(Modifier.padding(top = 16.dp)) {
                    RadioGroup(
                        modifier = Modifier
                            .weight(1f),
                        config = viewModel.messagePositionRadioGroup
                    )
                    CheckGroup(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp),
                        config = viewModel.messageAnimationCheckGroup
                    )
                }
                RadioGroup(
                    modifier = Modifier.padding(top = 16.dp),
                    config = viewModel.messageActionRadioGroup,
                    orientation = Orientation.HORIZONTAL
                )
                Button(
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = viewModel::onShowMessageClick
                ) {
                    Text(stringResource(R.string.show_message_button))
                }
            }
            InfoBar( // Generic InfoBar
                modifier = Modifier.padding(16.dp)
                    .align(viewModel.infoBarAlignment),
                offeredMessage = viewModel.infoBarMessage,
                fadeEffect = viewModel.infoBarFadeEffect,
                slideEffect = viewModel.infoBarSlideEffect,
                onMessageTimeout = viewModel::onInfoBarMessageTimeout
            )
            InfoBar( // Custom InfoBar
                modifier = Modifier.padding(16.dp)
                    .align(viewModel.infoBarAlignment),
                offeredMessage = viewModel.customInfoBarMessage,
                content = customInfoBarContent,
                fadeEffect = viewModel.infoBarFadeEffect,
                slideEffect = viewModel.infoBarSlideEffect,
                onMessageTimeout = viewModel::onCustomInfoBarMessageTimeout
            )
        }
    }
}

@ExperimentalAnimationApi
@Preview(
    showBackground = true
)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}
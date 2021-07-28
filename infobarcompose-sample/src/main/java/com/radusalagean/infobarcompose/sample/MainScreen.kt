package com.radusalagean.infobarcompose.sample

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.radusalagean.infobarcompose.InfoBar

@ExperimentalAnimationApi
@Composable
fun MainScreen(viewModel: MainViewModel = viewModel()) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column(
                Modifier
                    .wrapContentHeight()
                    .align(Alignment.Center)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = viewModel.textField,
                    onValueChange = viewModel::onTextValueChange,
                    label = { Text(stringResource(R.string.message_hint)) }
                )
                Button(
                    modifier = Modifier
                        .padding(top = 16.dp)
                        .align(Alignment.CenterHorizontally),
                    onClick = viewModel::onShowMessageClick
                ) {
                    Text(stringResource(R.string.show_message_button))
                }
            }
            InfoBar( // Generic InfoBar
                modifier = Modifier.align(Alignment.BottomCenter),
                offeredMessage = viewModel.infoBarMessage,
                slideEffect = viewModel.infoBarSlideEffect,
                onMessageTimeout = viewModel::onInfoBarMessageTimeout
            )
            InfoBar( // Custom InfoBar
                modifier = Modifier.align(viewModel.infoBarAlignment),
                offeredMessage = viewModel.customInfoBarMessage,
                content = customInfoBarContent,
                slideEffect = viewModel.infoBarSlideEffect,
                onMessageTimeout = viewModel::onCustomInfoBarMessageTimeout
            )
        }
    }
}
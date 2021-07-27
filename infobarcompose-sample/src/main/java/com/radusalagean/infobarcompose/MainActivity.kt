package com.radusalagean.infobarcompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.radusalagean.infobarcompose.ui.theme.InfoBarComposeTheme

class MainActivity : ComponentActivity() {
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoBarComposeTheme {
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
                        var infoBarMessage: InfoBarMessage? by remember { mutableStateOf(null) }
                        Column(
                            Modifier
                                .wrapContentHeight()
                                .align(Alignment.Center)
                        ) {
                            var textFieldState by remember { mutableStateOf("Example message") }
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = textFieldState,
                                onValueChange = { textFieldState = it },
                                label = { Text("Type a message...") }
                            )
                            Button(
                                modifier = Modifier
                                    .padding(top = 16.dp)
                                    .align(Alignment.CenterHorizontally),
                                onClick = {
                                    if (textFieldState.isNotBlank()) {
                                        infoBarMessage = InfoBarMessage(
                                            textString = textFieldState
                                        )
                                    }
                                }
                            ) {
                                Text("Show Message")
                            }
                        }
                        InfoBar(
                            modifier = Modifier.align(Alignment.TopCenter),
                            offeredMessage = infoBarMessage,
                            shape = CutCornerShape(bottomEnd = 8.dp)
                        ) {
                            infoBarMessage = null
                        }
                    }
                }
            }
        }
    }
}

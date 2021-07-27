package com.radusalagean.infobarcompose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.radusalagean.infobarcompose.BaseInfoBarMessage
import com.radusalagean.infobarcompose.InfoBar
import com.radusalagean.infobarcompose.InfoBarMessage
import com.radusalagean.infobarcompose.InfoBarSlideEffect
import com.radusalagean.infobarcompose.sample.ui.theme.InfoBarComposeTheme

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
                        var customInfoBarMessage: CustomInfoBarMessage? by remember { mutableStateOf(null) }
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
//                                        infoBarMessage = InfoBarMessage(
//                                            textString = textFieldState
//                                        )
                                        customInfoBarMessage = CustomInfoBarMessage(
                                            bannerImage = R.drawable.banner_1,
                                            textString = textFieldState,
                                            textColor = Color(0XFF084C61),
                                            actionString = "Dismiss",
                                            actionColor = Color(0xFF0FA7D6),
                                            displayTimeSeconds = 4
                                        ) {
                                            customInfoBarMessage = null
                                        }
//                                        customInfoBarMessage = CustomInfoBarMessage(
//                                            bannerImage = R.drawable.banner_2,
//                                            textString = textFieldState,
//                                            textColor = Color(0xFFA80D0D),
//                                            actionString = "Dismiss",
//                                            actionColor = Color(0xFFF03535),
//                                            displayTimeSeconds = 4
//                                        ) {
//                                            customInfoBarMessage = null
//                                        }
                                    }
                                }
                            ) {
                                Text("Show Message")
                            }
                        }
                        InfoBar(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            offeredMessage = customInfoBarMessage,
                            content = contentComposable,
                            slideEffect = InfoBarSlideEffect.FROM_BOTTOM
                        ) {
                            customInfoBarMessage = null
                        }
//                        InfoBar(
//                            modifier = Modifier.align(Alignment.BottomCenter),
//                            offeredMessage = infoBarMessage,
//                            shape = CutCornerShape(bottomEnd = 8.dp),
//                            slideEffect = InfoBarSlideEffect.FROM_BOTTOM
//                        ) {
//                            infoBarMessage = null
//                        }
                    }
                }
            }
        }
    }
}

private val contentComposable: @Composable (CustomInfoBarMessage) -> Unit = { message ->
    Box {
        var imageSize by remember { mutableStateOf(IntSize.Zero) }
        val gradient = Brush.linearGradient(
            start = Offset.Zero,
            end = Offset(x = imageSize.width.toFloat(), y = 0f),
            colorStops = arrayOf(
                0f to Color.White,
                0.7f to Color.White.copy(alpha = 0.8f),
                0.9f to Color.White.copy(alpha = 0.4f),
                1f to Color.White.copy(alpha = 0.3f)
            )
        )
        Image( // TODO fix landscape scale
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { imageSize = it.size },
            painter = painterResource(message.bannerImage),
            contentDescription = "Banner"
        )
        Row(
            modifier = Modifier
                .matchParentSize()
                .background(gradient)
                .padding(
                    horizontal = 12.dp,
                    vertical = 8.dp
                )
        ) {
            Text(
                text = message.textString,
                color = message.textColor ?: Color.DarkGray,
                modifier = Modifier
                    .align(Alignment.Top)
                    .weight(1f)
                    .padding(end = 8.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.h6
            )
            TextButton(
                modifier = Modifier.align(Alignment.Bottom),
                onClick = message.onAction,
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(
                    width = 1.dp,
                    color = message.actionColor ?: Color.Gray
                ),
                colors = ButtonDefaults.textButtonColors(
                    contentColor = message.actionColor ?: Color.Gray,
                    backgroundColor = message.actionBackgroundColor ?: Color.White
                )
            ) {
                Text(
                    text = message.actionString.uppercase(),
                    fontSize = 12.sp
                )
            }
        }
    }
}

class CustomInfoBarMessage(
    @DrawableRes val bannerImage: Int,
    val textString: String,
    val textColor: Color? = null,
    val actionString: String,
    val actionColor: Color? = null,
    val actionBackgroundColor: Color? = null,
    override val displayTimeSeconds: Int? = 4,
    val onAction: () -> Unit
) : BaseInfoBarMessage()
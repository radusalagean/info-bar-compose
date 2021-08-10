package com.radusalagean.infobarcompose

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalAccessibilityManager
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

internal const val INFO_BAR_CONTENT_DESCRIPTION = "InfoBar"

@Composable
fun <T : BaseInfoBarMessage> InfoBar(
    modifier: Modifier = Modifier,
    offeredMessage: T?,
    elevation: Dp = 6.dp,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color? = null,
    content: @Composable (T) -> Unit,
    fadeEffect: Boolean = true,
    scaleEffect: Boolean = true,
    slideEffect: InfoBarSlideEffect = InfoBarSlideEffect.NONE,
    enterTransitionMillis: Int = 150,
    exitTransitionMillis: Int = 75,
    wrapInsideBox: Boolean = true,
    onDismiss: () -> Unit
) {
    val displayedMessage: MutableState<T?> = remember { mutableStateOf(null) }
    val isShown: MutableState<Boolean> = remember { mutableStateOf(false) }
    val accessibilityManager = LocalAccessibilityManager.current

    suspend fun handleOfferedMessage() {
        isShown.value = false
        delay(InfoBarDelay.showDelay)
        displayedMessage.value = offeredMessage
        if (offeredMessage == null) return
        isShown.value = true
        delay(enterTransitionMillis.toLong())
        val delayTime = offeredMessage.getInfoBarTimeout(accessibilityManager)
        delay(delayTime)
        isShown.value = false
        delay(exitTransitionMillis.toLong())
        onDismiss()
    }

    LaunchedEffect(offeredMessage) {
        handleOfferedMessage()
    }

    InfoBarAnimation(
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                liveRegion = LiveRegionMode.Polite
                contentDescription = INFO_BAR_CONTENT_DESCRIPTION
                dismiss {
                    onDismiss()
                    true
                }
            },
        visible = isShown.value,
        fadeEffect = fadeEffect,
        scaleEffect = scaleEffect,
        slideEffect = slideEffect,
        enterTransitionMillis = enterTransitionMillis,
        exitTransitionMillis = exitTransitionMillis,
        wrapInsideBox = wrapInsideBox
    ) {
        displayedMessage.value?.let { message ->
            Surface(
                modifier = it,
                elevation = elevation,
                shape = shape,
                color = message.backgroundColor ?: backgroundColor ?: SnackbarDefaults.backgroundColor,
                contentColor = MaterialTheme.colors.surface
            ) {
                content(message)
            }
        }
    }
}

@Composable
fun InfoBar(
    modifier: Modifier = Modifier,
    offeredMessage: InfoBarMessage?,
    elevation: Dp = 6.dp,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color? = null,
    textColor: Color? = null,
    textFontSize: TextUnit = TextUnit.Unspecified,
    textFontStyle: FontStyle? = null,
    textFontWeight: FontWeight? = null,
    textFontFamily: FontFamily? = null,
    textLetterSpacing: TextUnit = TextUnit.Unspecified,
    textDecoration: TextDecoration? = null,
    textAlign: TextAlign? = null,
    textLineHeight: TextUnit = TextUnit.Unspecified,
    textMaxLines: Int = 5,
    textStyle: TextStyle = LocalTextStyle.current,
    actionColor: Color? = null,
    fadeEffect: Boolean = true,
    scaleEffect: Boolean = true,
    slideEffect: InfoBarSlideEffect = InfoBarSlideEffect.NONE,
    enterTransitionMillis: Int = 150,
    exitTransitionMillis: Int = 75,
    wrapInsideBox: Boolean = true,
    onDismiss: () -> Unit
) {
    val contentComposable: @Composable (InfoBarMessage) -> Unit = { message ->
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 6.dp,
                end = if (message.action != null) 8.dp else 16.dp,
                bottom = 6.dp
            )
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically),
                text = message.getTextString(),
                color = message.textColor ?: textColor ?: MaterialTheme.colors.surface,
                fontSize = textFontSize,
                fontStyle = textFontStyle,
                fontWeight = textFontWeight,
                fontFamily = textFontFamily,
                letterSpacing = textLetterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign ?: if (!message.getActionString().isNullOrBlank())
                    TextAlign.Start else TextAlign.Center,
                lineHeight = textLineHeight,
                overflow = TextOverflow.Ellipsis,
                maxLines = textMaxLines,
                style = textStyle
            )
            val actionString = message.getActionString()
            if (!actionString.isNullOrBlank()) {
                TextButton(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(start = 8.dp),
                    onClick = message.onAction ?: {},
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = message.actionColor ?: actionColor
                        ?: SnackbarDefaults.primaryActionColor
                    )
                ) {
                    Text(actionString)
                }
            }
        }
    }
    InfoBar(
        modifier = modifier,
        offeredMessage = offeredMessage,
        elevation = elevation,
        shape = shape,
        backgroundColor = backgroundColor,
        content = contentComposable,
        fadeEffect = fadeEffect,
        scaleEffect = scaleEffect,
        slideEffect = slideEffect,
        enterTransitionMillis = enterTransitionMillis,
        exitTransitionMillis = exitTransitionMillis,
        wrapInsideBox = wrapInsideBox,
        onDismiss = onDismiss
    )
}
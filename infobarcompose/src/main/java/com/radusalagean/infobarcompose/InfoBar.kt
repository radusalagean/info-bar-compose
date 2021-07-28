package com.radusalagean.infobarcompose

import androidx.compose.animation.*
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
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
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit

const val SHOW_DELAY = 200L

@ExperimentalAnimationApi
@Composable
fun <T : BaseInfoBarMessage> InfoBar(
    modifier: Modifier = Modifier,
    offeredMessage: T?,
    elevation: Dp = 6.dp,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color? = null,
    content: @Composable (T) -> Unit,
    fadeEffect: Boolean = true,
    slideEffect: InfoBarSlideEffect = InfoBarSlideEffect.FROM_TOP,
    enterTransitionMillis: Int = 150,
    exitTransitionMillis: Int = 250,
    onMessageTimeout: () -> Unit
) {
    val displayedMessage: MutableState<T?> = remember { mutableStateOf(null) }
    val isShown: MutableState<Boolean> = remember { mutableStateOf(false) }
    LaunchedEffect(offeredMessage) {
        handleOfferedMessage(
            offeredMessage = offeredMessage,
            displayedMessage = displayedMessage,
            isShown = isShown,
            onMessageTimeout = onMessageTimeout
        )
    }
    var enterTransition = EnterTransition.None
    var exitTransition = ExitTransition.None
    if (slideEffect != InfoBarSlideEffect.NONE) {
        enterTransition += slideInVertically(
            initialOffsetY = { fullHeight ->
                when (slideEffect) {
                    InfoBarSlideEffect.FROM_TOP -> -fullHeight
                    else -> fullHeight
                }
            },
            animationSpec = tween(
                durationMillis = enterTransitionMillis,
                easing = LinearOutSlowInEasing
            )
        )
        exitTransition += slideOutVertically(
            targetOffsetY = { fullHeight ->
                when (slideEffect) {
                    InfoBarSlideEffect.FROM_TOP -> -fullHeight
                    else -> fullHeight
                }
            },
            animationSpec = tween(
                durationMillis = exitTransitionMillis,
                easing = FastOutLinearInEasing
            )
        )
    }
    if (fadeEffect) {
        enterTransition += fadeIn(
            animationSpec = tween(
                durationMillis = enterTransitionMillis,
                easing = LinearOutSlowInEasing
            )
        )
        exitTransition += fadeOut(
            animationSpec = tween(
                durationMillis = exitTransitionMillis,
                easing = LinearOutSlowInEasing
            )
        )
    }
    AnimatedVisibility(
        modifier = modifier.fillMaxWidth(),
        visible = isShown.value,
        enter = enterTransition,
        exit = exitTransition
    ) {
        displayedMessage.value?.let { message ->
            Surface(
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

@ExperimentalAnimationApi
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
    slideEffect: InfoBarSlideEffect = InfoBarSlideEffect.FROM_TOP,
    enterTransitionMillis: Int = 150,
    exitTransitionMillis: Int = 250,
    onMessageTimeout: () -> Unit
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
                modifier = Modifier.weight(1f).align(Alignment.CenterVertically),
                text = message.text,
                color = message.textColor ?: textColor ?: MaterialTheme.colors.surface,
                fontSize = textFontSize,
                fontStyle = textFontStyle,
                fontWeight = textFontWeight,
                fontFamily = textFontFamily,
                letterSpacing = textLetterSpacing,
                textDecoration = textDecoration,
                textAlign = textAlign ?: if (message.action != null)
                    TextAlign.Start else TextAlign.Center,
                lineHeight = textLineHeight,
                overflow = TextOverflow.Ellipsis,
                maxLines = textMaxLines,
                style = textStyle
            )
            if (!message.action.isNullOrBlank()) {
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
                    Text(message.action)
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
        slideEffect = slideEffect,
        enterTransitionMillis = enterTransitionMillis,
        exitTransitionMillis = exitTransitionMillis,
        onMessageTimeout = onMessageTimeout
    )
}

private suspend fun <T : BaseInfoBarMessage> handleOfferedMessage(
    offeredMessage: T?,
    displayedMessage: MutableState<T?>,
    isShown: MutableState<Boolean>,
    onMessageTimeout: () -> Unit
) {
    isShown.value = false
    delay(SHOW_DELAY)
    displayedMessage.value = offeredMessage
    if (offeredMessage == null) return
    isShown.value = true
    val delayTime = offeredMessage.displayTimeSeconds
        .takeUnless { it == null || it <= 0 }?.toLong() ?: Long.MAX_VALUE
    delay(TimeUnit.SECONDS.toMillis(delayTime))
    isShown.value = false
    onMessageTimeout()
}

package com.radusalagean.infobarcompose

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalAccessibilityManager
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
import kotlinx.coroutines.*

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
    var displayedMessage: T? by remember { mutableStateOf(null) }
    var isShown: Boolean by remember { mutableStateOf(false) }
    val accessibilityManager = LocalAccessibilityManager.current
    val transition = updateTransition(
        targetState = isShown,
        label = "InfoBar - transition"
    )

    suspend fun handleOfferedMessage() {
        if (transition.currentState && transition.targetState) {
            isShown = false
            delay(exitTransitionMillis.toLong())
        }
        displayedMessage = offeredMessage
        if (offeredMessage == null) return
        isShown = true
        delay(enterTransitionMillis.toLong())
        val delayTime = offeredMessage.getInfoBarTimeout(accessibilityManager)
        delay(delayTime)
        isShown = false
        delay(exitTransitionMillis.toLong())
        onDismiss()
    }

    LaunchedEffect(offeredMessage) {
        handleOfferedMessage()
    }

    var contentHeightPx by remember { mutableStateOf(0) }
    var refreshRestingTranslationY by remember { mutableStateOf(false) }
    val restingTranslationY by remember(slideEffect, contentHeightPx) {
        derivedStateOf {
            when (slideEffect) {
                InfoBarSlideEffect.NONE -> 0f
                InfoBarSlideEffect.FROM_TOP -> -contentHeightPx.toFloat()
                InfoBarSlideEffect.FROM_BOTTOM -> contentHeightPx.toFloat()
            }
        }
    }
    val durationMillis = when {
        isShown -> enterTransitionMillis
        else -> exitTransitionMillis
    }
    val animatedAlpha by transition.animateFloat(
        label = "InfoBar - animatedAlpha",
        transitionSpec = {
            tween(
                easing = LinearEasing,
                durationMillis = durationMillis
            )
        }
    ) {
        if (it || !fadeEffect) 1f else 0f
    }
    val alpha by remember(refreshRestingTranslationY, contentHeightPx) {
        derivedStateOf {
            if (refreshRestingTranslationY || contentHeightPx == 0) 0f else animatedAlpha
        }
    }
    val animatedScale by transition.animateFloat(
        label = "InfoBar - animatedScale",
        transitionSpec = {
            tween(
                easing = FastOutSlowInEasing,
                durationMillis = durationMillis
            )
        }
    ) {
        if (it || !scaleEffect) 1f else 0.8f
    }
    val animatedTranslationY by transition.animateFloat(
        label = "InfoBar - animatedTranslationY",
        transitionSpec = {
            if (refreshRestingTranslationY || contentHeightPx == 0) snap() else tween(
                easing = FastOutSlowInEasing,
                durationMillis = durationMillis
            )
        }
    ) {
        if (!it || refreshRestingTranslationY) restingTranslationY else 0f
    }
    if (refreshRestingTranslationY && animatedTranslationY == restingTranslationY) {
        refreshRestingTranslationY = false
    }
    val surfaceComposable: @Composable (Modifier) -> Unit = {
        displayedMessage?.let { message ->
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
    if (displayedMessage != null) {
        val contentModifier = modifier
            .onSizeChanged {
                if (contentHeightPx != it.height) {
                    refreshRestingTranslationY = true
                    contentHeightPx = it.height
                }
            }
            .graphicsLayer(
                scaleX = animatedScale,
                scaleY = animatedScale,
                translationY = animatedTranslationY
            )
        if (wrapInsideBox) {
            /**
             * Note: Jetpack compose 1.0.0 will clip the shadow of an elevated item (in our case,
             *  the surface) when alpha is less than 1.0f. As a workaround, the content is wrapped
             *  inside a Box layout that fills the maximum available size. The alpha is then applied
             *  to that Box instead of the content. Disable this workaround by setting the
             *  wrapInsideBox flag to false when calling the InfoBar.
             */
            Box(
                Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = alpha)
            ) {
                surfaceComposable(contentModifier)
            }
        } else {
            surfaceComposable(
                contentModifier.graphicsLayer(alpha = alpha)
            )
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
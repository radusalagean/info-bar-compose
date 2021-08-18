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
import androidx.compose.ui.layout.onGloballyPositioned
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
import kotlinx.coroutines.*

internal const val INFO_BAR_TEST_TAG = "InfoBar"

/**
 * Composable that displays temporary on-screen messages (Generic Version)
 *
 * Use this generic version if your layout requirements can't be implemented with the
 * standard [InfoBar] composable.
 *
 * @param T [BaseInfoBarMessage] subclass, representing the data structure of the message
 * @param modifier Modifier to be applied to the [InfoBar] surface
 * @param offeredMessage [BaseInfoBarMessage] subclass instance, describing the message that
 * should be displayed
 * @param elevation Elevation to be applied to the [InfoBar] surface
 * @param shape Shape to be applied to the [InfoBar] surface
 * @param backgroundColor Background color to be applied to the [InfoBar] surface
 * @param content The content composable to use in the [InfoBar] surface
 * @param fadeEffect Use fading effect when the message appears and disappears?
 * (controls the `alpha` property)
 * @param fadeEffectEasing Easing style of the fade effect
 * @param scaleEffect Use scaling effect when the message appears and disappears?
 * (controls the `scaleX` / `scaleY` properties)
 * @param scaleEffectEasing Easing style of the scale effect
 * @param slideEffect Which sliding effect to use when the message appears and disappears?
 * (controls the `translationY` property)
 * @param slideEffectEasing Easing style of the slide effect
 * @param enterTransitionMillis Enter animation duration in milliseconds
 * @param exitTransitionMillis Exit animation duration in milliseconds
 * @param wrapInsideExpandedBox Maintain the shadow of the [InfoBar] even when animating the
 * `alpha` property, by wrapping the [InfoBar] content inside a [Box] layout that fills the maximum
 * available space. The `alpha` property is then animated on the outer [Box] instead of the [InfoBar]
 * surface, thus not clipping the shadow when `alpha` is less than `1f`. *Note: Any modifier you pass
 * from the outside is applied to the [InfoBar] surface, not the outer [Box] layout!*
 * @param onDismiss Function which is called when the [InfoBar] is either timed out or dismissed by
 * the user. *Don't forget to always null out the [BaseInfoBarMessage] subclass instance here!*
 */
@Composable
fun <T : BaseInfoBarMessage> InfoBar(
    modifier: Modifier = Modifier,
    offeredMessage: T?,
    elevation: Dp = 6.dp,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color? = null,
    content: @Composable (T) -> Unit,
    fadeEffect: Boolean = true,
    fadeEffectEasing: InfoBarEasing = InfoBarEasing(LinearEasing),
    scaleEffect: Boolean = true,
    scaleEffectEasing: InfoBarEasing = InfoBarEasing(FastOutSlowInEasing),
    slideEffect: InfoBarSlideEffect = InfoBarSlideEffect.NONE,
    slideEffectEasing: InfoBarEasing = InfoBarEasing(FastOutSlowInEasing),
    enterTransitionMillis: Int = 150,
    exitTransitionMillis: Int = 75,
    wrapInsideExpandedBox: Boolean = true,
    onDismiss: () -> Unit
) {

    fun isTransitionDelayNeeded() =
        fadeEffect || scaleEffect || slideEffect != InfoBarSlideEffect.NONE

    fun getEnterTransitionMillis() = if (isTransitionDelayNeeded()) enterTransitionMillis else 0
    fun getExitTransitionMillis() = if (isTransitionDelayNeeded()) exitTransitionMillis else 0

    var displayedMessage: T? by remember { mutableStateOf(null) }
    var isShown: Boolean by remember { mutableStateOf(false) }
    var contentMeasurePass: Boolean by remember { mutableStateOf(false) }
    val accessibilityManager = LocalAccessibilityManager.current
    val coroutineScope = rememberCoroutineScope()
    var showMessageJob: Job? by remember { mutableStateOf(null) }
    val transition = updateTransition(
        targetState = isShown,
        label = "InfoBar - transition"
    )

    fun InfoBarEasing.getEasing(): Easing = if (isShown) enterEasing else exitEasing

    suspend fun handleOfferedMessage() {
        if (transition.currentState && transition.targetState) {
            isShown = false
            delay(getExitTransitionMillis().toLong())
        } else if (transition.targetState) isShown = false
        showMessageJob?.cancel()
        if (offeredMessage != null) contentMeasurePass = true
        displayedMessage = offeredMessage
    }

    suspend fun showMessage() {
        displayedMessage?.let {
            isShown = true
            delay(getEnterTransitionMillis().toLong())
            val delayTime = it.getInfoBarTimeout(accessibilityManager)
            delay(delayTime)
            isShown = false
            delay(getExitTransitionMillis().toLong())
            onDismiss()
        }
    }

    LaunchedEffect(offeredMessage) {
        handleOfferedMessage()
    }

    var contentHeightPx by remember { mutableStateOf(0) }
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
        isShown -> getEnterTransitionMillis()
        else -> getExitTransitionMillis()
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
    if (contentMeasurePass) {
        surfaceComposable(
            Modifier
                .fillMaxWidth()
                .onGloballyPositioned {
                    if (!contentMeasurePass) return@onGloballyPositioned
                    if (contentHeightPx != it.size.height)
                        contentHeightPx = it.size.height
                    contentMeasurePass = false
                    showMessageJob?.cancel()
                    showMessageJob = coroutineScope.launch { showMessage() }
                }
                .graphicsLayer(alpha = 0f)
        )
    } else if (transition.currentState || transition.targetState) {
        val alpha by transition.animateFloat(
            label = "InfoBar - alpha",
            transitionSpec = {
                tween(
                    easing = fadeEffectEasing.getEasing(),
                    durationMillis = durationMillis
                )
            }
        ) {
            if (it || !fadeEffect) 1f else 0f
        }
        val scale by transition.animateFloat(
            label = "InfoBar - scale",
            transitionSpec = {
                tween(
                    easing = scaleEffectEasing.getEasing(),
                    durationMillis = durationMillis
                )
            }
        ) {
            if (it || !scaleEffect) 1f else 0.8f
        }
        val translationY by transition.animateFloat(
            label = "InfoBar - translationY",
            transitionSpec = {
                tween(
                    easing = slideEffectEasing.getEasing(),
                    durationMillis = durationMillis
                )
            }
        ) {
            if (!it) restingTranslationY else 0f
        }
        val contentModifier = modifier
            .fillMaxWidth()
            .graphicsLayer(
                scaleX = scale,
                scaleY = scale,
                translationY = translationY
            )
            .semantics {
                liveRegion = LiveRegionMode.Polite
                testTag = INFO_BAR_TEST_TAG
                dismiss {
                    onDismiss()
                    true
                }
            }
        if (wrapInsideExpandedBox) {
            /**
             * Note: Jetpack compose will clip the shadow of an elevated item (in our case,
             *  the surface) when alpha is less than 1.0f. As a workaround, the content is wrapped
             *  inside a Box layout that fills the maximum available size. The alpha is then applied
             *  to that Box instead of the content. Disable this workaround by setting the
             *  wrapInsideExpandedBox flag to false when calling the InfoBar.
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

/**
 * Composable that displays temporary on-screen messages (Standard Version)
 *
 * This version already has a layout defined, inspired by the Material design [Snackbar].
 * Use the generic version of the [InfoBar] and define your own layout, if you are unable to meet
 * your UI requirements with this standard one.
 *
 * @param modifier see generic [InfoBar]
 * @param offeredMessage see generic [InfoBar]
 * @param elevation see generic [InfoBar]
 * @param shape see generic [InfoBar]
 * @param backgroundColor see generic [InfoBar]
 * @param textVerticalPadding Vertical padding for the message text
 * @param textColor Color for the message text
 * @param textFontSize Font size for the message text
 * @param textFontStyle Font style for the message text
 * @param textFontWeight Font weight for the message text
 * @param textFontFamily Font family for the message text
 * @param textLetterSpacing Letter spacing for the message text
 * @param textDecoration Decoration for the message text
 * @param textAlign Alignment for the message text
 * @param textLineHeight Line height for the message text
 * @param textMaxLines Maximum number of lines for the message text
 * @param textStyle Style for the message text
 * @param actionColor Color for the action button text
 * @param fadeEffect see generic [InfoBar]
 * @param fadeEffectEasing see generic [InfoBar]
 * @param scaleEffect see generic [InfoBar]
 * @param scaleEffectEasing see generic [InfoBar]
 * @param slideEffect see generic [InfoBar]
 * @param slideEffectEasing see generic [InfoBar]
 * @param enterTransitionMillis see generic [InfoBar]
 * @param exitTransitionMillis see generic [InfoBar]
 * @param wrapInsideExpandedBox see generic [InfoBar]
 * @param onDismiss see generic [InfoBar]
 */
@Composable
fun InfoBar(
    modifier: Modifier = Modifier,
    offeredMessage: InfoBarMessage?,
    elevation: Dp = 6.dp,
    shape: Shape = MaterialTheme.shapes.small,
    backgroundColor: Color? = null,
    textVerticalPadding: Dp = 8.dp,
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
    fadeEffectEasing: InfoBarEasing = InfoBarEasing(LinearEasing),
    scaleEffect: Boolean = true,
    scaleEffectEasing: InfoBarEasing = InfoBarEasing(FastOutSlowInEasing),
    slideEffect: InfoBarSlideEffect = InfoBarSlideEffect.NONE,
    slideEffectEasing: InfoBarEasing = InfoBarEasing(FastOutSlowInEasing),
    enterTransitionMillis: Int = 150,
    exitTransitionMillis: Int = 75,
    wrapInsideExpandedBox: Boolean = true,
    onDismiss: () -> Unit
) {
    val contentComposable: @Composable (InfoBarMessage) -> Unit = { message ->
        Row(
            modifier = Modifier.padding(
                start = 16.dp,
                top = 6.dp,
                end = if (!message.getActionString().isNullOrBlank()) 8.dp else 16.dp,
                bottom = 6.dp
            )
        ) {
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.high) {
                Text(
                    modifier = Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically)
                        .padding(vertical = textVerticalPadding),
                    text = message.getTextString(),
                    color = message.textColor ?: textColor ?: MaterialTheme.colors.surface,
                    fontSize = textFontSize,
                    fontStyle = textFontStyle,
                    fontWeight = textFontWeight,
                    fontFamily = textFontFamily,
                    letterSpacing = textLetterSpacing,
                    textDecoration = textDecoration,
                    textAlign = textAlign,
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
    }
    InfoBar(
        modifier = modifier,
        offeredMessage = offeredMessage,
        elevation = elevation,
        shape = shape,
        backgroundColor = backgroundColor,
        content = contentComposable,
        fadeEffect = fadeEffect,
        fadeEffectEasing = fadeEffectEasing,
        scaleEffect = scaleEffect,
        scaleEffectEasing = scaleEffectEasing,
        slideEffect = slideEffect,
        slideEffectEasing = slideEffectEasing,
        enterTransitionMillis = enterTransitionMillis,
        exitTransitionMillis = exitTransitionMillis,
        wrapInsideExpandedBox = wrapInsideExpandedBox,
        onDismiss = onDismiss
    )
}
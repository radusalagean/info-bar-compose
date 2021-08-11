package com.radusalagean.infobarcompose

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged

@Composable
internal fun InfoBarAnimation(
    modifier: Modifier = Modifier,
    messageShown: Boolean,
    messageAvailable: Boolean,
    fadeEffect: Boolean,
    scaleEffect: Boolean,
    slideEffect: InfoBarSlideEffect,
    enterTransitionMillis: Int,
    exitTransitionMillis: Int,
    wrapInsideBox: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    val transition = updateTransition(
        targetState = messageShown,
        label = "InfoBarAnimation - transition"
    )
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
    val animatedAlpha by transition.animateFloat(
        label = "InfoBarAnimation - animatedAlpha",
        transitionSpec = {
            tween(
                easing = LinearEasing,
                durationMillis = if (messageShown) enterTransitionMillis else exitTransitionMillis
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
        label = "InfoBarAnimation - animatedScale",
        transitionSpec = {
            tween(
                easing = FastOutSlowInEasing,
                durationMillis = if (messageShown) enterTransitionMillis else exitTransitionMillis
            )
        }
    ) {
        if (it || !scaleEffect) 1f else 0.8f
    }
    val animatedTranslationY by transition.animateFloat(
        label = "InfoBarAnimation - animatedTranslationY",
        transitionSpec = {
            if (refreshRestingTranslationY || contentHeightPx == 0) snap() else tween(
                easing = FastOutSlowInEasing,
                durationMillis = if (messageShown) enterTransitionMillis else exitTransitionMillis
            )
        }
    ) {
        if (!it || refreshRestingTranslationY) restingTranslationY else 0f
    }
    if (refreshRestingTranslationY && animatedTranslationY == restingTranslationY) {
        refreshRestingTranslationY = false
    }
    if (messageAvailable) {
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
                content(contentModifier)
            }
        } else {
            content(
                contentModifier.graphicsLayer(alpha = alpha)
            )
        }
    }
}
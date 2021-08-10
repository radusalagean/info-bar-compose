package com.radusalagean.infobarcompose

import android.util.Log
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
    visible: Boolean,
    fadeEffect: Boolean,
    scaleEffect: Boolean,
    slideEffect: InfoBarSlideEffect,
    enterTransitionMillis: Int,
    exitTransitionMillis: Int,
    wrapInsideBox: Boolean,
    content: @Composable (Modifier) -> Unit
) {
    val transitionState = remember { MutableTransitionState(false) }
    val transition = updateTransition(transitionState = transitionState, label = "InfoBarAnimation - transition")
    LaunchedEffect(visible) {
        transitionState.targetState = visible
    }
    var contentHeightPx by remember { mutableStateOf(0) }
    val animatedAlpha by transition.animateFloat(
        label = "InfoBarAnimation - animatedAlpha",
        transitionSpec = {
            tween(
                easing = LinearEasing,
                durationMillis = if (visible) enterTransitionMillis else exitTransitionMillis
            )
        }
    ) {
        if (it || !fadeEffect) 1f else 0f
    }
    val animatedScale by transition.animateFloat(
        label = "InfoBarAnimation - animatedScale",
        transitionSpec = {
            tween(
                easing = FastOutSlowInEasing,
                durationMillis = if (visible) enterTransitionMillis else exitTransitionMillis
            )
        }
    ) {
        if (it || !scaleEffect) 1f else 0.8f
    }
    val animatedTranslationY by transition.animateFloat(
        label = "InfoBarAnimation - animatedTranslationY",
        transitionSpec = {
            tween(
                easing = FastOutSlowInEasing,
                durationMillis = if (visible) enterTransitionMillis else exitTransitionMillis
            )
        }
    ) {
        if (!it) {
            when (slideEffect) {
                InfoBarSlideEffect.NONE -> 0f
                InfoBarSlideEffect.FROM_TOP -> -contentHeightPx.toFloat()
                InfoBarSlideEffect.FROM_BOTTOM -> contentHeightPx.toFloat()
//                InfoBarSlideEffect.FROM_TOP -> -133f
//                InfoBarSlideEffect.FROM_BOTTOM -> 133f
            }
        } else 0f
    }
    if (transitionState.currentState || transitionState.targetState) {
        val contentModifier = modifier
            .onSizeChanged {
                if (contentHeightPx != it.height) {
                    contentHeightPx = it.height
                    Log.d("mytag", "content height assigned")
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
                    .graphicsLayer(alpha = animatedAlpha)
            ) {
                content(contentModifier)
            }
        } else {
            content(
                contentModifier.graphicsLayer(alpha = animatedAlpha)
            )
        }
    }
}
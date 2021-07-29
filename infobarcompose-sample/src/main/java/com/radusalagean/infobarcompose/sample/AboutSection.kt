package com.radusalagean.infobarcompose.sample

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

private const val TAG = "URL"

@Composable
fun AboutSection(modifier: Modifier = Modifier) {
    val annotatedString = buildAnnotatedString {
        pushStyle(style = SpanStyle(fontSize = 16.sp))
        append(stringResource(R.string.about_version, BuildConfig.VERSION_NAME))
        append("\n\n")
        append(stringResource(R.string.about_info))
        append("\n\n")
        pop()
        pushStringAnnotation(
            tag = TAG,
            annotation = stringResource(id = R.string.about_github_link)
        )
        pushStyle(
            style = SpanStyle(
                color = MaterialTheme.colors.secondary,
                fontSize = 16.sp,
                textDecoration = TextDecoration.Underline
            )
        )
        append(stringResource(R.string.about_github_link))
        pop()
        pop()
    }
    val uriHandler = LocalUriHandler.current
    ClickableText(
        modifier = modifier,
        text = annotatedString,
        style = TextStyle.Default.copy(textAlign = TextAlign.Center)
    ) {
        annotatedString.getStringAnnotations(tag = TAG, it, it).firstOrNull()?.let {
            uriHandler.openUri(it.item)
        }
    }
}
package com.radusalagean.infobarcompose.sample

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val customInfoBarContent: @Composable (CustomInfoBarMessage) -> Unit = { message ->
    Box(
        Modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
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
        Image(
            modifier = Modifier
                .matchParentSize()
                .align(Alignment.Center)
                .onGloballyPositioned { imageSize = it.size },
            painter = painterResource(message.bannerImage),
            contentDescription = null,
            contentScale = ContentScale.Crop
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
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 22.sp,
                fontFamily = FontFamily.SansSerif,
                lineHeight = 28.sp
            )
            if (message.actionStringResId != null) {
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
                        backgroundColor = message.actionBackgroundColor
                            ?: Color.White.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = stringResource(message.actionStringResId),
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}
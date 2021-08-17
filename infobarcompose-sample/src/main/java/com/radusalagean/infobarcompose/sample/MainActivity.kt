package com.radusalagean.infobarcompose.sample

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.radusalagean.infobarcompose.sample.ui.theme.InfoBarComposeTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            InfoBarComposeTheme {
                MainScreen()
            }
        }
    }
}
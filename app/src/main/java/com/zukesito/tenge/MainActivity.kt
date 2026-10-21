package com.zukesito.tenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.zukesito.tenge.presentation.navigation.TengeApp
import com.zukesito.tenge.presentation.theme.TengeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TengeTheme {
                TengeApp()
            }
        }
    }
}

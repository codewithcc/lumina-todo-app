package com.codewithcc.lumina.model

import kotlinx.serialization.Serializable

@Serializable
sealed class Screens() {
    @Serializable data object Splash: Screens()
    @Serializable data object Main: Screens()
}
package com.example.musicplayer.model


import java.io.Serializable


data class AudioModel(
    val title:String,
    val artist:String,
    val path:String,
    val duration:String
):Serializable

package com.springimage.models

data class ImageSize(var sizeName: String, var width: Int, var height: Int) {
    val totalPixels:Int = width * height
}
package com.springimage.models

import java.util.*

/**
 * Created by n0288764 on 8/11/16.
 */
data class ImageRecord(var imageBytes: ByteArray, var size: ImageSize, var format: FileType) {
    /**
     * Outputs just a Base64 encoded representation of the image, without any file format data.
     */
    fun toBase64(): String = Base64.getEncoder().encodeToString(this.imageBytes)

    /**
     * Outputs a Base64 encoded representation of the image with full data URI components.
     */
    fun toHtmlBase64(): String = "data:${this.format.mime()};base64,${toBase64()}"
}
package com.springimage.utils

import com.springimage.models.FileType
import com.springimage.models.ImageRecord
import com.springimage.models.ImageSize
import com.springimage.models.getFileTypeFromExtension
import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Created by n0288764 on 8/10/16.
 *
 * Contains methods for scaling images
 */

class Scaler {
    private val img: BufferedImage
    private val format: FileType

    constructor(uploadedImage: MultipartFile) {
        this.img = ImageIO.read(ByteArrayInputStream(uploadedImage.bytes))
        this.format = getFileTypeFromExtension(uploadedImage.originalFilename)
    }
    constructor(uploadedImage: BufferedImage, format: FileType) {
        this.img = uploadedImage
        this.format = format
    }
    constructor(uploadedImage: BufferedImage, name: String) {
        this.img = uploadedImage
        this.format = getFileTypeFromExtension(name)
    }
    constructor(uploadedImage: ImageRecord) {
        this.img = ImageIO.read(ByteArrayInputStream(uploadedImage.imageBytes))
        this.format = uploadedImage.format
    }

    fun ScaleImage(imageSize: ImageSize): ImageRecord {
        return ScaleImage(imageSize.width, imageSize.height, this.img, imageSize)
    }

    /**
     * Scales an imageData to exact width and height or proportionately by a single side
     *
     * @param width desired width of the new imageData or 0 if resizing by height only
     * @param height desired height of the new imageData or 0 if resizing by width only
     */
    fun ScaleImage(width:Int = 0, height:Int = 0): ImageRecord {
        val normalizedHeight = if (height == 0) {
            (width * img.height) / img.width
        } else {
            height
        }
        val normalizedWidth = if (width == 0) {
            (height * img.width) / img.height
        } else {
            width
        }

        return ScaleImage(normalizedWidth, normalizedHeight, img)
    }

    /**
     * Scales an imageData so the longest side is the length of edge in pixels
     *
     * @param edge length of the longest side in pixels after resizing
     */
    fun ScaleImageLongEdge(edge:Int): ImageRecord {
        if (img.height > img.width) { // Resize by height
            return ScaleImage(height = edge)
        }
        else { // Resize by width or square imageData
            return ScaleImage(width = edge)
        }
    }

    /**
     * Scales an imageData so the shortest side is the length of edge in pixels
     *
     * @param edge length of the shortest side in pixels after resizing
     */
    fun ScaleImageShortEdge(edge: Int): ImageRecord {
        if (img.height < img.width) { // Resize by height
            return ScaleImage(height = edge)
        }
        else { // Resize by width or square imageData
            return ScaleImage(width = edge)
        }
    }

    private fun ScaleImage(width:Int, height:Int, img: BufferedImage, imageSize: ImageSize? = null):ImageRecord {
        val scaledImage: Image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH)
        val imageBuff: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
        imageBuff.graphics.drawImage(scaledImage, 0, 0, null, null)
        val outputStream = ByteArrayOutputStream()
        ImageIO.write(imageBuff, "png", outputStream) //TODO: Support other imageData formats
        if (imageSize != null) {
            return ImageRecord(outputStream.toByteArray(), imageSize, this.format)
        }
        else {
            return ImageRecord(outputStream.toByteArray(), ImageSize("${width}_${height}", width, height), this.format)
        }
    }
}
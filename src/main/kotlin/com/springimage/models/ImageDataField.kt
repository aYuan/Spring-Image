package com.springimage.models

import com.springimage.utils.Scaler
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

/**
 * Created by n0288764 on 8/11/16.
 */
data class ImageDataField(var name: String = "", var extension: String = "", var format: FileType? = null, var imageData: MutableMap<String, ImageRecord> = mutableMapOf()) {

    @JvmOverloads
    fun addItemImage(uploadedImage: MultipartFile, size: ImageSize? = null) {
        val img: BufferedImage = ImageIO.read(ByteArrayInputStream(uploadedImage.bytes))
        val imgSize:ImageSize = size ?: ImageSize("original", img.width, img.height) // Create default original size if no other size is specified
        if (format == null) {
            format = getFileTypeFromExtension(uploadedImage.originalFilename)
        }
        val imgRecord:ImageRecord = when {
            size != null -> Scaler(img).ScaleImage(size)
            else -> ImageRecord(uploadedImage.bytes, imgSize)
        }
        imageData[imgSize.sizeName] = imgRecord
    }

    @JvmOverloads
    fun addItemImage(uploadedImage: MultipartFile, sizes: List<ImageSize>, storeOriginal: Boolean = true) {
        if (storeOriginal && !sizes.any({ it.sizeName == "original"})) {
            addItemImage(uploadedImage) // Add original
        }
        sizes.forEach { size ->
            addItemImage(uploadedImage, size)
        }
    }

    @JvmOverloads
    fun getItemImageBySize(sizeKey: String = "original"): ImageRecord? {
        if (!imageData.containsKey(sizeKey)) {
            return null
        }
        else {
            return imageData[sizeKey]
        }
    }

    fun getItemImageBySize(width: Int, height: Int): ImageRecord {
        if (imageData.containsKey("${width}_${height}")) { // Try to return a pre-rendered copy if one exists
            return imageData["${width}_${height}"]!!
        }
        val scaleSource: ImageRecord = ScaleSource()
        return Scaler(scaleSource).ScaleImage(width, height)
    }

    fun getItemImageBySizeLongEdge(longEdge: Int): ImageRecord {
        val scaleSource: ImageRecord = ScaleSource()
        return Scaler(scaleSource).ScaleImageLongEdge(longEdge)
    }

    fun getItemImageBySizeShortEdge(shortEdge: Int): ImageRecord {
        val scaleSource: ImageRecord = ScaleSource()
        return Scaler(scaleSource).ScaleImageShortEdge(shortEdge)
    }

    private fun LargestImage(): ImageRecord = imageData.maxBy { t -> t.value.size.totalPixels }!!.value
    private fun ScaleSource(): ImageRecord = if (imageData.containsKey("original")) getItemImageBySize("original")!! else LargestImage()
}

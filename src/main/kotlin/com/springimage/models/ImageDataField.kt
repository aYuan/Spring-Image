package com.springimage.models

import com.springimage.utils.Scaler
import org.springframework.web.multipart.MultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import javax.imageio.ImageIO

/**
 * Created by n0288764 on 8/11/16.
 */
data class ImageDataField(var name: String = "", var extension: String = "", var format: FileTypes? = null, var imageData: MutableMap<String, ImageRecord> = mutableMapOf()) {

    @JvmOverloads
    fun addItemImage(uploadedImage: MultipartFile, size: ImageSize? = null) {
        if (format == null) {
            format = getFileType(uploadedImage.originalFilename)
        }

        val img: BufferedImage = ImageIO.read(ByteArrayInputStream(uploadedImage.bytes))
        val imgSize:ImageSize = size ?: ImageSize("original", img.width, img.height) // Create default original size if no other size is specified

        val imgRecord:ImageRecord = ImageRecord(uploadedImage.bytes, imgSize)
        imageData[imgSize.sizeName] = imgRecord
    }

    fun addItemImage(uploadedImage: MultipartFile, sizes: List<ImageSize>) {
        sizes.forEach { size ->
            addItemImage(uploadedImage, size)
        }
    }

    fun getItemImageBySize(sizeKey: String): ImageRecord? {
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

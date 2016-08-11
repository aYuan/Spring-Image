package com.utils

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
 * @property uploadedImage original image that is to be resized
 */

internal class Scaler(uploadedImage: MultipartFile) {
    private val originalSizeImg: ByteArray = uploadedImage.bytes;
    private val img: BufferedImage = ImageIO.read(ByteArrayInputStream(originalSizeImg));

    /**
     * Scales an image to exact width and height or proportionately by a single side
     *
     * @param width desired width of the new image or 0 if resizing by height only
     * @param height desired height of the new image or 0 if resizing by width only
     */
    fun ScaleImage(width:Int = 0, height:Int = 0): ByteArray {
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

        return ScaleImage(normalizedWidth, normalizedHeight, img);
    }

    /**
     * Scales an image so the longest side is the length of edge in pixels
     *
     * @param edge length of the longest side in pixels after resizing
     */
    fun ScaleImageLongEdge(edge:Int): ByteArray {
        if (img.height > img.width) { // Resize by height
            return ScaleImage(height = edge);
        }
        else { // Resize by width or square image
            return ScaleImage(width = edge);
        }
    }

    /**
     * Scales an image so the shortest side is the length of edge in pixels
     *
     * @param edge length of the shortest side in pixels after resizing
     */
    fun ScaleImageShortEdge(edge: Int): ByteArray {
        if (img.height < img.width) { // Resize by height
            return ScaleImage(height = edge);
        }
        else { // Resize by width or square image
            return ScaleImage(width = edge);
        }
    }

    private fun ScaleImage(width:Int, height:Int, img: BufferedImage):ByteArray {
        val scaledImage: Image = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        val imageBuff: BufferedImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        imageBuff.graphics.drawImage(scaledImage, 0, 0, null, null);
        val outputStream = ByteArrayOutputStream();
        ImageIO.write(imageBuff, "png", outputStream); //TODO: Support other image formats
        return outputStream.toByteArray();
    }
}
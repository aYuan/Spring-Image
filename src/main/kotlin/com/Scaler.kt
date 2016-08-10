package com

import org.springframework.web.multipart.MultipartFile
import java.awt.Image
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

/**
 * Created by n0288764 on 8/10/16.
 */

class Scaler(uploadedImage: MultipartFile) {
    private var originalSizeImg: ByteArray = uploadedImage.bytes;

    fun ScaleImage(width:Int = 0, height:Int = 0): ByteArray {
        val inputStream: ByteArrayInputStream = ByteArrayInputStream(originalSizeImg);
        val img: BufferedImage = ImageIO.read(inputStream);

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

        val scaledImage:Image = img.getScaledInstance(normalizedWidth, normalizedHeight, Image.SCALE_SMOOTH);
        val imageBuff:BufferedImage = BufferedImage(normalizedWidth, normalizedHeight, BufferedImage.TYPE_INT_ARGB);
        imageBuff.graphics.drawImage(scaledImage, 0, 0, null, null);
        val outputStream = ByteArrayOutputStream();
        ImageIO.write(imageBuff, "png", outputStream); //TODO: Support other image formats
        return outputStream.toByteArray();
    }
}
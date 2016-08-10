package com

import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.test.assertEquals

class ScalerTest {
    private var imageScaler: Scaler? = null

    @Before
    @Throws(IOException::class)
    fun setup() {
        val file = File("src/test/resources/testImg.png")
        val input = FileInputStream(file)
        val image = MockMultipartFile("testImage", file.name, "image/jpeg", IOUtils.toByteArray(input))
        this.imageScaler = Scaler(image)
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownWidth() {
        val scaledImage = this.imageScaler!!.ScaleImage(500, 500)
        val imageStream = ByteArrayInputStream(scaledImage)
        val img = ImageIO.read(imageStream)
        assertEquals(500, img.width.toLong(), "Scaler didn't scale down width properly")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownHeight() {
        val scaledImage = this.imageScaler!!.ScaleImage(500, 500)
        val imageStream = ByteArrayInputStream(scaledImage)
        val img = ImageIO.read(imageStream)
        assertEquals(500, img.height.toLong(), "Scaler didn't scale down height properly")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownHeightProportion() {
        val scaledImage = this.imageScaler!!.ScaleImage(500, 0)
        val imageStream = ByteArrayInputStream(scaledImage)
        val img = ImageIO.read(imageStream)
        assertEquals(500, img.height.toLong(), "Scaler didn't scale down height proportionately")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownWidthProportion() {
        val scaledImage = this.imageScaler!!.ScaleImage(0, 500)
        val imageStream = ByteArrayInputStream(scaledImage)
        val img = ImageIO.read(imageStream)
        assertEquals(500, img.width.toLong(), "Image Scaler didn't scale down width proportionately")
    }
}

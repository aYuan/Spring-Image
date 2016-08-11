package com

import com.utils.Scaler
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockMultipartFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.imageio.ImageIO
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ScalerTest {
    private var squareScaler: Scaler? = null
    private var longScaler: Scaler? = null
    private var tallScaler: Scaler? = null

    @Before
    @Throws(IOException::class)
    fun setup() {
        val squareImageFile = File("src/test/resources/testImg.png")
        val squareInput = FileInputStream(squareImageFile)
        val squareImage = MockMultipartFile("testImage", squareImageFile.name, "image/jpeg", IOUtils.toByteArray(squareInput))

        val longImageFile = File("src/test/resources/testImgLongWidth.png")
        val longInput = FileInputStream(longImageFile)
        val longImage = MockMultipartFile("testImage", squareImageFile.name, "image/jpeg", IOUtils.toByteArray(longInput))

        val tallImageFile = File("src/test/resources/testImgLongHeight.png")
        val tallInput = FileInputStream(tallImageFile)
        val tallImage = MockMultipartFile("testImage", squareImageFile.name, "image/jpeg", IOUtils.toByteArray(tallInput))

        this.squareScaler = Scaler(squareImage)
        this.longScaler = Scaler(longImage)
        this.tallScaler = Scaler(tallImage)
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleUpWidth() {
        val scaledImage = this.squareScaler!!.ScaleImage(500, 500)
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.width.toLong(), "Scaler didn't scale up width properly")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleUpHeight() {
        val scaledImage = this.squareScaler!!.ScaleImage(500, 500)
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.height.toLong(), "Scaler didn't scale up height properly")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleUpHeightProportion() {
        val scaledImage = this.squareScaler!!.ScaleImage(500, 0)
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.height.toLong(), "Scaler didn't scale up height proportionately")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleUpWidthProportion() {
        val scaledImage = this.squareScaler!!.ScaleImage(0, 500)
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.width.toLong(), "Image Scaler didn't scale up width proportionately")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownWidth() {
        val scaledImage = this.squareScaler!!.ScaleImage(50, 50)
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.width.toLong(), "Scaler didn't scale down width properly")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownHeight() {
        val scaledImage = this.squareScaler!!.ScaleImage(50, 50)
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.height.toLong(), "Scaler didn't scale down height properly")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownHeightProportion() {
        val scaledImage = this.squareScaler!!.ScaleImage(50, 0)
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.height.toLong(), "Scaler didn't scale down height proportionately")
    }

    @Test
    @Throws(IOException::class)
    fun testImageScaleDownWidthProportion() {
        val scaledImage = this.squareScaler!!.ScaleImage(0, 50)
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.width.toLong(), "Image Scaler didn't scale down width proportionately")
    }

    @Test
    fun testImageScaleUpLongSideHeight() {
        val scaledImage = this.tallScaler!!.ScaleImageLongEdge(500);
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.height.toLong(), "Image Scaler didn't scale the height properly")
        assertTrue(img.height.toLong() > img.width.toLong(), "Image Scaler didn't scale along the longest edge.")
    }

    @Test
    fun testImageScaleDownLongSideHeight() {
        val scaledImage = this.tallScaler!!.ScaleImageLongEdge(50);
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.height.toLong(), "Image Scaler didn't scale the height properly")
        assertTrue(img.height.toLong() > img.width.toLong(), "Image Scaler didn't scale along the longest edge")
    }

    @Test
    fun testImageScaleUpLongSideWidth() {
        val scaledImage = this.longScaler!!.ScaleImageLongEdge(500);
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.width.toLong(), "Image Scaler didn't scale the width properly")
        assertTrue(img.width.toLong() > img.height.toLong(), "Image Scaler didn't scale along the longest edge")
    }

    @Test
    fun testImageScaleDownLongSideWidth() {
        val scaledImage = this.longScaler!!.ScaleImageLongEdge(50);
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.width.toLong(), "Image Scaler didn't scale the width properly")
        assertTrue(img.width.toLong() > img.height.toLong(), "Image Scaler didn't scale along the longest edge")
    }

    @Test
    fun testImageScaleUpShortSideHeight() {
        val scaledImage = this.longScaler!!.ScaleImageShortEdge(500);
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.height.toLong(), "Image Scaler didn't scale the height properly")
        assertTrue(img.width.toLong() > img.height.toLong(), "Image Scaler didn't scale along the shortest edge")
    }

    @Test
    fun testImageScaleDownShortSideHeight() {
        val scaledImage = this.longScaler!!.ScaleImageShortEdge(50);
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.height.toLong(), "Image Scaler didn't scale the height properly")
        assertTrue(img.width.toLong() > img.height.toLong(), "Image Scaler didn't scale along the shortest edge")
    }

    @Test
    fun testImageScaleUpShortSideWidth() {
        val scaledImage = this.tallScaler!!.ScaleImageShortEdge(500);
        val img = getBufferedImage(scaledImage)
        assertEquals(500, img.width.toLong(), "Image Scaler didn't scale the height properly")
        assertTrue(img.height.toLong() > img.width.toLong(), "Image Scaler didn't scale along the shortest edge")
    }

    @Test
    fun testImageScaleDownShortSideWidth() {
        val scaledImage = this.tallScaler!!.ScaleImageShortEdge(50);
        val img = getBufferedImage(scaledImage)
        assertEquals(50, img.width.toLong(), "Image Scaler didn't scale the height properly")
        assertTrue(img.height.toLong() > img.width.toLong(), "Image Scaler didn't scale along the shortest edge")
    }

    @Test
    fun testImageScaleShortEdgeSquare() {
        val scaledImage = this.squareScaler!!.ScaleImageShortEdge(50)
        val img = getBufferedImage(scaledImage)
        assertTrue(img.height.toLong() == img.width.toLong(), "Image Scaler failed to scale a square image equally")
    }

    @Test
    fun testImageScaleLongEdgeSquare() {
        val scaledImage = this.squareScaler!!.ScaleImageLongEdge(50)
        val img = getBufferedImage(scaledImage)
        assertTrue(img.height.toLong() == img.width.toLong(), "Image Scaler failed to scale a square image equally")
    }

    private fun getBufferedImage(inputImage:ByteArray): BufferedImage {
        return ImageIO.read(ByteArrayInputStream(inputImage))
    }
}

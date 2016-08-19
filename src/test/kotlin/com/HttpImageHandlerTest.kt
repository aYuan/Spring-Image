package com

import com.springimage.models.ImageDataField
import com.springimage.utils.HttpImageHandler.Companion.getSize
import com.springimage.utils.HttpImageHandler.Companion.getSizeLongEdge
import com.springimage.utils.HttpImageHandler.Companion.getSizeShortEdge
import org.apache.commons.io.IOUtils
import org.junit.Before
import org.junit.Test
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.mock.web.MockMultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import javax.imageio.ImageIO
import kotlin.test.assertEquals


class HttpImageHandlerTest {
    private val squareImageData:ImageDataField = ImageDataField()
    private val tallImageData:ImageDataField = ImageDataField()
    private val longImageData:ImageDataField = ImageDataField()
    private val response:MockHttpServletResponse = MockHttpServletResponse()

    @Before
    fun setup() {
        val squareImageFile = File("src/test/resources/testImg.png")
        val squareInput = FileInputStream(squareImageFile)
        val squareImage = MockMultipartFile("testImage", squareImageFile.name, "image/png", IOUtils.toByteArray(squareInput))

        val longImageFile = File("src/test/resources/testImgLongWidth.png")
        val longInput = FileInputStream(longImageFile)
        val longImage = MockMultipartFile("testImage", squareImageFile.name, "imageData/jpeg", IOUtils.toByteArray(longInput))

        val tallImageFile = File("src/test/resources/testImgLongHeight.png")
        val tallInput = FileInputStream(tallImageFile)
        val tallImage = MockMultipartFile("testImage", squareImageFile.name, "imageData/jpeg", IOUtils.toByteArray(tallInput))

        this.squareImageData.addItemImage(squareImage)
        this.longImageData.addItemImage(longImage)
        this.tallImageData.addItemImage(tallImage)
    }

    @Test
    fun testCacheControlHeader() {
        this.squareImageData.getSize(50, 50, response)
        assert(response.containsHeader("Cache-Control"))
    }

    @Test
    fun testPragmaHeader() {
        this.squareImageData.getSize(50, 50, response)
        assert(response.containsHeader("Pragma"))
    }

    @Test
    fun testExpiresHeader() {
        this.squareImageData.getSize(50, 50, response)
        assert(response.containsHeader("Expires"))
    }

    @Test
    fun testMimeContentType() {
        this.squareImageData.getSize(50, 50, response)
        assertEquals("image/png", response.contentType)
    }

    @Test
    fun testGetSize() {
        this.squareImageData.getSize(50, 50, response)
        val img = ImageIO.read(ByteArrayInputStream(response.contentAsByteArray))
        assertEquals(50, img.width)
        assertEquals(50, img.height)
    }

    @Test
    fun testGetSizeImageSize() {
        this.squareImageData.getSize("original", response)
        val img = ImageIO.read(ByteArrayInputStream(response.contentAsByteArray))
        assertEquals(150, img.width)
    }

    @Test
    fun testGetSizeImageSizeNotSaved() {
        this.squareImageData.getSize("FakeSize", response)
        assertEquals(response.contentAsString, "Not Found")
    }

    @Test
    fun testGetSizeLongEdge() {
        this.tallImageData.getSizeLongEdge(100, response)
        val img = ImageIO.read(ByteArrayInputStream(response.contentAsByteArray))
        assertEquals(100, img.height)
        assert(img.width < 100)
    }

    @Test
    fun testGetSizeShortEdge() {
        this.longImageData.getSizeShortEdge(100, response)
        val img = ImageIO.read(ByteArrayInputStream(response.contentAsByteArray))
        assertEquals(100, img.height)
        assert(img.width > 100)
    }
}
package com.springimage.utils

import com.springimage.models.ImageDataField
import com.springimage.models.ImageRecord
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse

/**
 * Created by n0288764 on 8/11/16.
 */
class HttpImageHandler {
    companion object {

        @JvmStatic
        fun ImageDataField.getSize(sizeKey: String, response: HttpServletResponse) {
            val responseOutputStream: ServletOutputStream = response.outputStream
            val img = this.getItemImageBySize(sizeKey)

            if (img != null) {
                setHeaders(response)
                response.contentType = this.format!!.mime()
                responseOutputStream.write(img.imageBytes)
            }
            else {
                responseOutputStream.write("Not Found".toByteArray())
            }

            responseOutputStream.flush()
            responseOutputStream.close()
        }

        @JvmStatic
        fun ImageDataField.getSize(width: Int, height: Int, response: HttpServletResponse) {
            writeResponse(this.getItemImageBySize(width, height), this.format!!.mime(), response)
        }

        @JvmStatic
        fun ImageDataField.getSizeLongEdge(longEdge: Int, response: HttpServletResponse) {
            writeResponse(this.getItemImageBySizeLongEdge(longEdge), this.format!!.mime(), response)
        }

        @JvmStatic
        fun ImageDataField.getSizeShortEdge(shortEdge: Int, response: HttpServletResponse) {
            writeResponse(this.getItemImageBySizeShortEdge(shortEdge), this.format!!.mime(), response)
        }

        private fun writeResponse(image: ImageRecord, mime: String, response: HttpServletResponse) {
            val responseOutputStream: ServletOutputStream = response.outputStream
            setHeaders(response)
            response.contentType = mime
            responseOutputStream.write(image.imageBytes)
            responseOutputStream.flush()
            responseOutputStream.close()
        }

        private fun setHeaders(response: HttpServletResponse) {
            if (!response.containsHeader("Cache-Control")) {
                response.setHeader("Cache-Control", "no-store")
            }
            if (!response.containsHeader("Pragma")) {
                response.setHeader("Pragma", "no-cache")
            }
            if (!response.containsHeader("Expires")) {
                response.setDateHeader("Expires", 0)
            }
        }

    }
}
package com.models

/**
 * Created by n0288764 on 8/11/16.
 */
enum class FileTypes {
    PNG {
        override fun extensions() = arrayOf("png")
        override fun mime() = "imageData/png"
    },
    JPEG {
        override fun extensions() = arrayOf("jpg", "jpeg", "jfif", "jfif-tbnl", "jpe")
        override fun mime() = "imageData/jpeg"
    },
    TIFF {
        override fun extensions() = arrayOf("tif", "tiff")
        override fun mime() = "imageData/tiff"
    },
    BMP {
        override fun extensions() = arrayOf("bmp", "bm")
        override fun mime() = "imageData/bmp"
    },
    GIF {
        override fun extensions() = arrayOf("gif")
        override fun mime() = "imageData/gif"
    };

    fun extensionsContains(fileName: String): Boolean {
        return extensions().filter { ext -> fileName.contains(ext) }.count() > 0
    }

    abstract fun extensions(): Array<String>
    abstract fun mime(): String
}

fun getFileType(fileName: String): FileTypes {
    return if (FileTypes.JPEG.extensionsContains(fileName)) {
        FileTypes.JPEG
    }
    else if (FileTypes.PNG.extensionsContains(fileName)) {
        FileTypes.PNG
    }
    else if (FileTypes.TIFF.extensionsContains(fileName)) {
        FileTypes.TIFF
    }
    else if (FileTypes.BMP.extensionsContains(fileName)) {
        FileTypes.BMP
    }
    else if (FileTypes.GIF.extensionsContains(fileName)) {
        FileTypes.GIF
    }
    else {
        throw NotImplementedError()
    }
}
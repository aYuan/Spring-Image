package com.springimage.models

/**
 * Created by n0288764 on 8/11/16.
 */
enum class FileType {
    PNG {
        override fun extensions() = arrayOf("png")
        override fun mime() = "image/png"
    },
    JPEG {
        override fun extensions() = arrayOf("jpg", "jpeg", "jfif", "jfif-tbnl", "jpe")
        override fun mime() = "image/jpeg"
    },
    TIFF {
        override fun extensions() = arrayOf("tif", "tiff")
        override fun mime() = "image/tiff"
    },
    BMP {
        override fun extensions() = arrayOf("bmp", "bm")
        override fun mime() = "image/bmp"
    },
    GIF {
        override fun extensions() = arrayOf("gif")
        override fun mime() = "image/gif"
    };

    fun extensionsContains(fileName: String): Boolean {
        return extensions().filter { ext -> fileName.contains(ext) }.count() > 0
    }

    abstract fun extensions(): Array<String>
    abstract fun mime(): String
}

fun getFileType(fileName: String): FileType {
    return if (FileType.JPEG.extensionsContains(fileName)) {
        FileType.JPEG
    }
    else if (FileType.PNG.extensionsContains(fileName)) {
        FileType.PNG
    }
    else if (FileType.TIFF.extensionsContains(fileName)) {
        FileType.TIFF
    }
    else if (FileType.BMP.extensionsContains(fileName)) {
        FileType.BMP
    }
    else if (FileType.GIF.extensionsContains(fileName)) {
        FileType.GIF
    }
    else {
        throw NotImplementedError()
    }
}
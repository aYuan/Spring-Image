package com.models

/**
 * Created by n0288764 on 8/11/16.
 */
enum class FileTypes {
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

    abstract fun extensions(): Array<String>
    abstract fun mime(): String
}
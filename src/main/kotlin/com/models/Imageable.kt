package com.models

interface Imageable {
    fun getImage(): ImageDataField;
    fun getImage(key: String): ImageDataField;
    fun setImage(key: String, imageData: ImageDataField);
    fun hasImage(): Boolean;
}
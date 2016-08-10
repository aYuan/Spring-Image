package com.models

interface Imageable {
    fun getImage(): MutableMap<String, ByteArray>;
    fun getImage(key: String): ByteArray;
    fun setImage(key: String, image: ByteArray);
    fun hasImage(): Boolean;
}
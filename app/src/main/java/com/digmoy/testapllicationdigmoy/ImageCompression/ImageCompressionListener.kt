package com.tutorbunny.tutorbunny.ImageCompression

interface ImageCompressionListener {
    fun onStart()
    fun onCompressed(filePath: String?)
}

package com.model

data class UploadedFile(
    val name: String,
    val uri: android.net.Uri,
    val type: FileType,
    val addedAt: Long = System.currentTimeMillis() // This field tracks when the file was uploaded
)

enum class FileType {
    IMAGE,
    DOCUMENT
}

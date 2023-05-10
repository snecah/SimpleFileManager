package com.example.simplefilemanager.model

data class FileData(
    val fileName: String,
    val fileSize: String,
    val fileExtension: String,
    val lastModifiedDate: String,
    val filePath: String,
    val isDirectory: Boolean
)
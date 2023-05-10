package com.example.simplefilemanager

import java.text.SimpleDateFormat
import java.util.Locale

class Utils {
    companion object {
        private const val FORMAT_DATE_PATTERN = "dd/MM/yyyy HH:mm"
        val videoExtensions = listOf<String>(
            "3gp",
            "3gp",
            "3gpp2",
            "asf",
            "avi",
            "dv",
            "m2t",
            "m4v",
            "mkv",
            "mov",
            "mp4",
            "mpeg",
            "mpg",
            "mts",
            "oggtheora",
            "ogv",
            "rm",
            "ts",
            "vob",
            "webm",
            "wmv",
            "flv"
        )

        val audioExtensions = listOf<String>("aac", "flac", "m4a", "mp3", "oga", "wav", "wma")
        val textExtensions = listOf<String>("txt", "csv", "rtf", "odt", "md", "doc", "docx")
        val imageExtensions = listOf<String>("bmp", "gif", "jpeg", "jpg", "png", "tif", "tiff", "svg")
        const val pdfExtension = "pdf"


        fun formatDate(date: Long): String {
            val formatter = SimpleDateFormat(FORMAT_DATE_PATTERN, Locale.US)
            return formatter.format(date)
        }

        fun getSize(length: Long): String {
            val fileSizeInKB = length / 1024
            val fileSizeInMB = fileSizeInKB / 1024
            return if (fileSizeInMB == 0L)
                "${fileSizeInKB}KB"
            else
                "${fileSizeInMB}MB"
        }
    }
}